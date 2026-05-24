package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CrmViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.FollowUpCard
import com.example.ui.theme.BgApp
import com.example.ui.theme.OnSurfaceColor
import com.example.ui.theme.OnSurfaceVariantColor
import com.example.ui.theme.OutlineVariantColor

@Composable
fun FollowUpsScreen(
    viewModel: CrmViewModel,
    modifier: Modifier = Modifier
) {
    val followUps by viewModel.followUps.collectAsStateWithLifecycle()

    val pendingTasks = followUps.filter { !it.done }
    val completedTasks = followUps.filter { it.done }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgApp)
    ) {
        // Toolbar section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Today's Follow-ups",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceColor
            )
            Text(
                text = "Scheduled appointments and outreach status updates",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariantColor
            )
        }

        if (pendingTasks.isEmpty() && completedTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.Default.AssignmentTurnedIn,
                    title = "All Done for Today!",
                    subtitle = "You have no scheduled follow-ups left. Enjoy the high-performance streak!"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Incomplete Tasks List
                if (pendingTasks.isNotEmpty()) {
                    items(pendingTasks, key = { it.id }) { task ->
                        FollowUpCard(
                            followup = task,
                            onToggle = { viewModel.toggleFollowUp(task) }
                        )
                    }
                } else {
                    item {
                        EmptyState(
                            icon = Icons.Default.AssignmentTurnedIn,
                            title = "Nothing Pending",
                            subtitle = "all pending tasks are marked as finished! Splendid job."
                        )
                    }
                }

                // Header Divider for Completed Section (if active)
                if (completedTasks.isNotEmpty()) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Divider(
                                color = OutlineVariantColor,
                                thickness = 1.dp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Completed Today",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariantColor,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Divider(
                                color = OutlineVariantColor,
                                thickness = 1.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Completed Tasks List
                    items(completedTasks, key = { it.id }) { task ->
                        FollowUpCard(
                            followup = task,
                            onToggle = { viewModel.toggleFollowUp(task) }
                        )
                    }
                }
            }
        }
    }
}
