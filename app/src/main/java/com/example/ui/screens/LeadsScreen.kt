package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CrmViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.LeadCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsScreen(
    viewModel: CrmViewModel,
    onNavigateToLeadDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchVal by viewModel.searchText.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val leads by viewModel.filteredLeads.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var isSearchFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgApp)
    ) {
        // App bar section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Inbound Leads",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceColor
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar with focus outline variant styling
            val searchBorderColor = if (isSearchFocused) PrimaryColor else Color.Transparent
            TextField(
                value = searchVal,
                onValueChange = { viewModel.updateSearchText(it) },
                placeholder = {
                    Text(
                        "Search customer name or summary...",
                        color = OnSurfaceVariantColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = OnSurfaceVariantColor
                    )
                },
                trailingIcon = {
                    if (searchVal.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchText("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = OnSurfaceVariantColor
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BgElevated,
                    unfocusedContainerColor = BgElevated,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = OnSurfaceColor,
                    unfocusedTextColor = OnSurfaceColor
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, searchBorderColor, RoundedCornerShape(24.dp))
                    .onFocusChanged { isSearchFocused = it.isFocused }
                    .testTag("leads_search_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Chips Horizontal List
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "all" to "All",
                    "whatsapp" to "WhatsApp",
                    "email" to "Email",
                    "call" to "Call"
                )

                filters.forEach { (key, display) ->
                    val isActive = activeFilter == key
                    val chipBg = if (isActive) PrimaryContainerColor else Color.Transparent
                    val chipBorderColor = if (isActive) Color.Transparent else OutlineVariantColor
                    val chipTextColor = if (isActive) OnPrimaryContainerColor else OnSurfaceColor

                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = chipBg,
                        border = if (isActive) null else BorderStroke(1.dp, chipBorderColor),
                        modifier = Modifier
                            .clickable { viewModel.updateFilter(key) }
                            .testTag("filter_chip_$key")
                    ) {
                        Text(
                            text = display,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = chipTextColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Leads Lazy Column
        if (leads.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.Default.SearchOff,
                    title = "No Leads Found",
                    subtitle = "We couldn't find any leads matching \"$searchVal\" or channel \"$activeFilter\"."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // Cushioning bottom elements
            ) {
                items(leads, key = { it.id }) { lead ->
                    LeadCard(
                        lead = lead,
                        onClick = { onNavigateToLeadDetail(lead.id) }
                    )
                }
            }
        }
    }
}
