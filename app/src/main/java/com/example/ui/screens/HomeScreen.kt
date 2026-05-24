package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CrmViewModel
import com.example.ui.components.ActivityFeedItem
import com.example.ui.components.AnalyticsChartsSection
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.utils.TimeHelpers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CrmViewModel,
    onNavigateToTab: (String) -> Unit,
    onNavigateToLeadDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rawLeads by viewModel.allLeads.collectAsStateWithLifecycle()
    val leads by viewModel.filteredLeads.collectAsStateWithLifecycle()
    val escalations by viewModel.escalations.collectAsStateWithLifecycle()
    val followUps by viewModel.followUps.collectAsStateWithLifecycle()

    val pendingFollowUpsCount = followUps.count { !it.done }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgApp)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp) // Leave roomy spacing for bottom navigation
    ) {
        // Welcome Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Good morning, Arjun 👋",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = TimeHelpers.formatHeaderDate(),
                style = MaterialTheme.typography.labelMedium,
                color = OnSurfaceVariantColor
            )
        }

        // Stats Grid Layout (2x2)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Total Leads Today",
                    value = "${rawLeads.size}",
                    icon = Icons.Default.TrendingUp,
                    accentColor = PrimaryColor,
                    trendLabel = "+12%",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Missed Enquiries",
                    value = "8",
                    icon = Icons.Default.ChatBubbleOutline,
                    accentColor = ErrorColor,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Open Escalations",
                    value = "${escalations.size}",
                    icon = Icons.Default.Warning,
                    accentColor = SecondaryColor,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Follow-ups Due",
                    value = "$pendingFollowUpsCount",
                    icon = Icons.Default.CalendarToday,
                    accentColor = PrimaryContainerColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Analytics Overview (Interactive custom Donut & Bar chart visualizations)
        AnalyticsChartsSection(
            leads = rawLeads,
            onFilterChannel = { filterKey ->
                viewModel.updateFilter(filterKey)
                onNavigateToTab("leads")
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Quick Actions Scroll Row
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineMedium,
                color = OnSurfaceColor,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Action 1: View Leads
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = PrimaryContainerColor,
                    modifier = Modifier
                        .clickable { onNavigateToTab("leads") }
                        .testTag("quick_action_view_leads")
                ) {
                    Text(
                        text = "View Leads",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnPrimaryContainerColor
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }

                // Action 2: Resolve Now
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = BgElevated,
                    border = BorderStroke(1.dp, OutlineVariantColor),
                    modifier = Modifier
                        .clickable { onNavigateToTab("escalations") }
                        .testTag("quick_action_resolve_now")
                ) {
                    Text(
                        text = "Resolve Now",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }

                // Action 3: Check Follow-ups
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = BgElevated,
                    border = BorderStroke(1.dp, OutlineVariantColor),
                    modifier = Modifier
                        .clickable { onNavigateToTab("followups") }
                        .testTag("quick_action_check_followups")
                ) {
                    Text(
                        text = "Check Followups",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Recent Activity Feed Block
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.headlineMedium,
                color = OnSurfaceColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leads.take(5).forEach { lead ->
                    ActivityFeedItem(
                        lead = lead,
                        onClick = { onNavigateToLeadDetail(lead.id) }
                    )
                }
            }
        }
    }
}
