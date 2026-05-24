package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeadEntity
import com.example.ui.theme.*

@Composable
fun AnalyticsChartsSection(
    leads: List<LeadEntity>,
    onFilterChannel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var chartType by remember { mutableStateOf("channel") } // "channel" | "status"
    var viewMode by remember { mutableStateOf("donut") }    // "donut" | "bar"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("analytics_charts_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = BgElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariantColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Interactive Analytics",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = OnSurfaceColor
                    )
                    Text(
                        text = "Click segments to filter CRM view",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariantColor
                    )
                }

                // Chart Selection Toggles
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewMode = "donut" },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (viewMode == "donut") PrimaryContainerColor else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DonutLarge,
                            contentDescription = "Donut Chart",
                            tint = if (viewMode == "donut") OnPrimaryContainerColor else OnSurfaceVariantColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewMode = "bar" },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (viewMode == "bar") PrimaryContainerColor else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Bar Chart",
                            tint = if (viewMode == "bar") OnPrimaryContainerColor else OnSurfaceVariantColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab-style view switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgApp, shape = RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "channel" to "Leads By Channel",
                    "status" to "Leads By Status"
                ).forEach { (key, label) ->
                    val isSelected = chartType == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BgCard else Color.Transparent)
                            .clickable { chartType = key }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = if (isSelected) PrimaryColor else OnSurfaceVariantColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display chart based on selected type
            val dataPoints = remember(leads, chartType) {
                if (chartType == "channel") {
                    val whatsappCount = leads.count { it.channel.equals("whatsapp", ignoreCase = true) }
                    val emailCount = leads.count { it.channel.equals("email", ignoreCase = true) }
                    val callCount = leads.count { it.channel.equals("call", ignoreCase = true) || it.channel.equals("phone", ignoreCase = true) }
                    val total = (whatsappCount + emailCount + callCount).coerceAtLeast(1)

                    listOf(
                        ChartDataPoint("WhatsApp", whatsappCount, total, WhatsAppColor, "whatsapp"),
                        ChartDataPoint("Email", emailCount, total, EmailColor, "email"),
                        ChartDataPoint("Calls", callCount, total, CallColor, "call")
                    )
                } else {
                    val newCount = leads.count { it.status.equals("new", ignoreCase = true) }
                    val qualifiedCount = leads.count { it.status.equals("qualified", ignoreCase = true) }
                    val escalatedCount = leads.count { it.status.equals("escalated", ignoreCase = true) }
                    val total = (newCount + qualifiedCount + escalatedCount).coerceAtLeast(1)

                    listOf(
                        ChartDataPoint("New", newCount, total, EmailColor, "all"), // Fallbacks to general list
                        ChartDataPoint("Qualified", qualifiedCount, total, StatusQualifiedColor, "all"),
                        ChartDataPoint("Escalated", escalatedCount, total, StatusEscalatedColor, "all")
                    )
                }
            }

            AnimatedContent(
                targetState = Pair(viewMode, chartType),
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith
                            fadeOut(animationSpec = tween(220))
                },
                label = "ChartTransition"
            ) { (currentViewMode, _) ->
                if (currentViewMode == "donut") {
                    DonutChartView(
                        dataPoints = dataPoints,
                        onSelectSegment = { item ->
                            if (chartType == "channel") {
                                onFilterChannel(item.filterKey)
                            }
                        }
                    )
                } else {
                    BarChartView(
                        dataPoints = dataPoints,
                        onSelectSegment = { item ->
                            if (chartType == "channel") {
                                onFilterChannel(item.filterKey)
                            }
                        }
                    )
                }
            }
        }
    }
}

data class ChartDataPoint(
    val label: String,
    val value: Int,
    val total: Int,
    val color: Color,
    val filterKey: String
) {
    val percentage: Float
        get() = if (total > 0) (value.toFloat() / total) * 100f else 0f
}

@Composable
fun DonutChartView(
    dataPoints: List<ChartDataPoint>,
    onSelectSegment: (ChartDataPoint) -> Unit
) {
    var hoveredIndex by remember { mutableStateOf(-1) }
    val animatedProgress = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "DonutAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: The Live Canvas Chart
        Box(
            modifier = Modifier
                .size(160.dp)
                .weight(1.1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .testTag("donut_canvas")
            ) {
                var startAngle = -90f
                val strokeWidth = 18.dp.toPx()

                dataPoints.forEachIndexed { index, point ->
                    val sweepAngle = (point.percentage / 100f) * 360f * animatedProgress.value
                    val isHovered = hoveredIndex == index
                    val extraStroke = if (isHovered) 8.dp.toPx() else 0f

                    drawArc(
                        color = point.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth + extraStroke, cap = StrokeCap.Round),
                        size = Size(size.width - strokeWidth - 10.dp.toPx(), size.height - strokeWidth - 10.dp.toPx()),
                        topLeft = Offset(strokeWidth / 2 + 5.dp.toPx(), strokeWidth / 2 + 5.dp.toPx())
                    )
                    startAngle += (point.percentage / 100f) * 360f
                }
            }

            // Inside Center UI
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                val activePoint = if (hoveredIndex in dataPoints.indices) dataPoints[hoveredIndex] else null
                if (activePoint != null) {
                    Text(
                        text = "${String.format("%.1f", activePoint.percentage)}%",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = OnSurfaceColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = activePoint.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariantColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    val totalSum = dataPoints.sumOf { it.value }
                    Text(
                        text = "$totalSum",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = OnSurfaceColor
                    )
                    Text(
                        text = "Total Leads",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariantColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right side: Interactive Legend
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            dataPoints.forEachIndexed { index, point ->
                val isSelected = hoveredIndex == index
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) BgCard else Color.Transparent)
                        .clickable {
                            hoveredIndex = if (isSelected) -1 else index
                            onSelectSegment(point)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(point.color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = point.label,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = OnSurfaceColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${point.value} leads",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariantColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BarChartView(
    dataPoints: List<ChartDataPoint>,
    onSelectSegment: (ChartDataPoint) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    val animatedProgress = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.FastOutSlowInEasing),
        label = "BarChartAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 8.dp)
    ) {
        // The Bars Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            dataPoints.forEachIndexed { index, point ->
                val isSelected = selectedIndex == index
                val barHeightFraction = (point.percentage / 100f) * animatedProgress.value

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Value Tooltip popup when highlighted
                    if (isSelected) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = BgCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariantColor),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "${point.value} (${String.format("%.1f", point.percentage)}%)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = PrimaryColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Bar graphic with custom gradient Fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(barHeightFraction.coerceIn(0.05f, 1f))
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        point.color,
                                        point.color.copy(alpha = 0.5f)
                                    )
                                )
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) OnSurfaceColor else Color.Transparent,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .clickable {
                                selectedIndex = if (isSelected) -1 else index
                                onSelectSegment(point)
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // X-Axis Labels Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            dataPoints.forEach { point ->
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = OnSurfaceVariantColor,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
