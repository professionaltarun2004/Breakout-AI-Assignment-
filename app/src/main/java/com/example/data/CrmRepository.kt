package com.example.data

import com.example.data.mock.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CrmRepository(
    private val leadDao: LeadDao,
    private val escalationDao: EscalationDao,
    private val followUpDao: FollowUpDao
) {
    val allLeads: Flow<List<LeadEntity>> = leadDao.getAllLeads()
    val allEscalations: Flow<List<EscalationEntity>> = escalationDao.getAllEscalations()
    val allFollowUps: Flow<List<FollowUpEntity>> = followUpDao.getAllFollowUps()

    fun getLeadById(id: String): Flow<LeadEntity?> {
        return leadDao.getLeadById(id)
    }

    suspend fun insertLead(lead: LeadEntity) {
        leadDao.insertLead(lead)
    }

    suspend fun updateLead(lead: LeadEntity) {
        leadDao.updateLead(lead)
    }

    suspend fun deleteEscalationById(id: String) {
        escalationDao.deleteEscalationById(id)
    }

    suspend fun updateFollowUp(followUp: FollowUpEntity) {
        followUpDao.updateFollowUp(followUp)
    }

    suspend fun checkAndPrepopulate() {
        try {
            val leads = leadDao.getAllLeads().first()
            if (leads.isEmpty()) {
                leadDao.insertLeads(MockData.initialLeads)
                escalationDao.insertEscalations(MockData.initialEscalations)
                followUpDao.insertFollowUps(MockData.initialFollowUps)
            }
        } catch (e: Exception) {
            // Safe fallback if flow.first() fails or database is empty but closed
            leadDao.insertLeads(MockData.initialLeads)
            escalationDao.insertEscalations(MockData.initialEscalations)
            followUpDao.insertFollowUps(MockData.initialFollowUps)
        }
    }
}
