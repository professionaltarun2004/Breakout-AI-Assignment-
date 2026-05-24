package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CrmViewModel(private val repository: CrmRepository) : ViewModel() {

    // Inputs for Filtering
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _activeFilter = MutableStateFlow("all") // all | whatsapp | email | call
    val activeFilter: StateFlow<String> = _activeFilter.asStateFlow()

    // 0. Expose all leads raw for statistics, charts and metrics
    val allLeads: StateFlow<List<LeadEntity>> = repository.allLeads
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 1. Reactive combined Leads block based on search and selected tab filter
    val filteredLeads: StateFlow<List<LeadEntity>> = combine(
        repository.allLeads,
        _searchText,
        _activeFilter
    ) { leads, query, filter ->
        leads.filter { lead ->
            val matchesChannel = (filter == "all") || lead.channel.equals(filter, ignoreCase = true)
            val matchesQuery = query.isBlank() || 
                    lead.customer.contains(query, ignoreCase = true) || 
                    lead.summary.contains(query, ignoreCase = true)
            matchesChannel && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 2. Active Escalations (Sorted: High Urgency First)
    val escalations: StateFlow<List<EscalationEntity>> = repository.allEscalations
        .map { list ->
            list.sortedByDescending { it.urgency.equals("high", ignoreCase = true) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 3. Follow Ups
    val followUps: StateFlow<List<FollowUpEntity>> = repository.allFollowUps
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Pre-populate database with default items on first cold boot
        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    fun updateSearchText(query: String) {
        _searchText.value = query
    }

    fun updateFilter(filter: String) {
        _activeFilter.value = filter
    }

    // Resolve an Escalation (removes it from persistence)
    fun resolveEscalation(id: String) {
        viewModelScope.launch {
            repository.deleteEscalationById(id)
        }
    }

    // Mark a Follow Up Done/Not Done
    fun toggleFollowUp(followUp: FollowUpEntity) {
        viewModelScope.launch {
            val updated = followUp.copy(done = !followUp.done)
            repository.updateFollowUp(updated)
        }
    }

    fun getLeadById(id: String): Flow<LeadEntity?> {
        return repository.getLeadById(id)
    }
}

class CrmViewModelFactory(private val repository: CrmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CrmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
