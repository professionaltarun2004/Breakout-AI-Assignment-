package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.CrmViewModel
import com.example.ui.components.EmptyState
import com.example.ui.components.EscalationCard
import com.example.ui.theme.BgApp
import com.example.ui.theme.ErrorColor
import com.example.ui.theme.OnErrorColor
import com.example.ui.theme.OnSurfaceColor

@Composable
fun EscalationsScreen(
    viewModel: CrmViewModel,
    onNavigateToLeadDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val escalations by viewModel.escalations.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgApp)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Active Escalations",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurfaceColor
                )
                Text(
                    text = "High priority customer issues needing SLA action",
                    style = MaterialTheme.typography.bodySmall,
                    color = com.example.ui.theme.OnSurfaceVariantColor
                )
            }

            // Count red pill
            if (escalations.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = ErrorColor,
                    modifier = Modifier.testTag("escalation_count_badge")
                ) {
                    Text(
                        text = "${escalations.size} Pending",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnErrorColor
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Escalations List
        if (escalations.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.Default.CheckCircle,
                    title = "All Clear!",
                    subtitle = "No active escalations. Every SLA threshold is satisfied."
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
                items(escalations, key = { it.id }) { escalation ->
                    EscalationCard(
                        escalation = escalation,
                        onResolve = {
                            viewModel.resolveEscalation(escalation.id)
                        },
                        onClick = {
                            onNavigateToLeadDetail(escalation.id)
                        }
                    )
                }
            }
        }
    }
}
