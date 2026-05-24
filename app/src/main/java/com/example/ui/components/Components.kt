package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EscalationEntity
import com.example.data.FollowUpEntity
import com.example.data.LeadEntity
import com.example.data.Message
import com.example.ui.theme.*
import com.example.utils.TimeHelpers

/**
 * 1. Channel Badge
 */
@Composable
fun ChannelBadge(
    channel: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, contentColor, icon, text) = when (channel.lowercase()) {
        "whatsapp" -> Quadruple(
            Color(0xFF0F301B),
            Color(0xFF25D366),
            Icons.Default.Chat,
            "WhatsApp"
        )
        "email" -> Quadruple(
            Color(0xFF0D1E3C),
            Color(0xFF3B82F6),
            Icons.Default.Email,
            "Email"
        )
        else -> Quadruple(
            Color(0xFF3C2800),
            Color(0xFFF59E0B),
            Icons.Default.Phone,
            "Call"
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = bgColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        }
    }
}

/**
 * 2. Status Badge
 */
@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, contentColor, text) = when (status.lowercase()) {
        "new" -> Triple(Color(0xFF0B1B3C), Color(0xFF3B82F6), "New")
        "qualified" -> Triple(Color(0xFF022B1F), Color(0xFF41EEC2), "Qualified")
        else -> Triple(Color(0xFF3C0E0E), Color(0xFFFFB4AB), "Escalated")
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = bgColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(contentColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text.substring(0, 1).uppercase() + text.substring(1),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = contentColor
            )
        }
    }
}

/**
 * 3. Urgency Badge
 */
@Composable
fun UrgencyBadge(
    urgency: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, contentColor, icon, text) = when (urgency.lowercase()) {
        "high" -> Quadruple(
            Color(0xFF3D0C0C),
            Color(0xFFFFB4AB),
            Icons.Default.Whatshot,
            "High"
        )
        else -> Quadruple(
            Color(0xFF3D2500),
            Color(0xFFF59E0B),
            Icons.Default.PriorityHigh,
            "Medium"
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = bgColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        }
    }
}

/**
 * 4. Stat Card for metric grids
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    trendLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BgElevated),
        border = BorderStroke(1.dp, OutlineVariantColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                // Round circular icon layout
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurfaceColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = OnSurfaceVariantColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (trendLabel != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF0F301B),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = trendLabel,
                        color = Color(0xFF41EEC2),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 5. Lead Card for lists
 */
@Composable
fun LeadCard(
    lead: LeadEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("lead_card_${lead.id}"),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, OutlineVariantColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Row (Avatar, Name, Time)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BgHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lead.initials,
                        style = MaterialTheme.typography.titleLarge,
                        color = OnSurfaceColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1.0f)) {
                    Text(
                        text = lead.customer,
                        style = MaterialTheme.typography.titleLarge,
                        color = OnSurfaceColor
                    )
                    Text(
                        text = lead.source,
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceVariantColor
                    )
                }

                Text(
                    text = TimeHelpers.timeAgo(lead.receivedAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariantColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body text
            Text(
                text = lead.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = OutlineVariantColor, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChannelBadge(channel = lead.channel)
                    StatusBadge(status = lead.status)
                }
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = OnSurfaceVariantColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 6. Escalation Card
 */
@Composable
fun EscalationCard(
    escalation: EscalationEntity,
    onResolve: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressing by remember { mutableStateOf(false) }
    val scaleFactor by animateFloatAsState(
        targetValue = if (isPressing) 0.95f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )

    val leftBorderColor = if (escalation.urgency.lowercase() == "high") ErrorColor else WarningColor
    val cardBg = if (escalation.urgency.lowercase() == "high") ErrorContainerColor else BgCard
    val borderStrokeColor = if (escalation.urgency.lowercase() == "high") ErrorColor else OutlineVariantColor
    val customerTextColor = if (escalation.urgency.lowercase() == "high") OnErrorColor else OnSurfaceColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleFactor)
            .clickable(onClick = onClick)
            .testTag("escalation_card_${escalation.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderStrokeColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            // thick left indicator border
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .background(leftBorderColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Top Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = escalation.customer,
                        style = MaterialTheme.typography.titleLarge,
                        color = customerTextColor
                    )
                    UrgencyBadge(urgency = escalation.urgency)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Detail message
                Text(
                    text = escalation.reason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariantColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChannelBadge(channel = escalation.channel)

                    // Custom styled "Resolve →" button list triggering animation
                    Button(
                        onClick = {
                            isPressing = true
                            // Auto reset state and trigger resolve handler
                            onResolve()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgElevated,
                            contentColor = PrimaryColor
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .testTag("resolve_button_${escalation.id}")
                            .height(36.dp)
                    ) {
                        Text(
                            text = "Resolve →",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 7. Follow Up Card
 */
@Composable
fun FollowUpCard(
    followup: FollowUpEntity,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine opacity and font decoration depending on task status
    val opacity = if (followup.done) 0.55f else 1.00f
    val textDecoration = if (followup.done) TextDecoration.LineThrough else TextDecoration.None

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(opacity)
            .testTag("follow_up_card_${followup.id}"),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, OutlineVariantColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Radio-styled check circle (48dp Touch Area)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        enabled = !followup.done, // Freeze interactions when resolved
                        onClick = onToggle
                    )
                    .testTag("follow_up_checkbox_${followup.id}"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (followup.done) PrimaryContainerColor else Color.Transparent)
                        .border(
                            2.dp,
                            if (followup.done) PrimaryContainerColor else PrimaryColor,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (followup.done) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = OnPrimaryContainerColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                // Name & Metadata strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = followup.customer,
                        style = MaterialTheme.typography.titleLarge.copy(textDecoration = textDecoration),
                        color = OnSurfaceColor
                    )

                    val dueTextColor = if (followup.overdue && !followup.done) ErrorColor else OnSurfaceVariantColor
                    Text(
                        text = TimeHelpers.formatDueTime(followup.dueAt, followup.overdue),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = dueTextColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = followup.messagePreview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariantColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                ChannelBadge(channel = followup.channel)
            }
        }
    }
}

/**
 * 8. Message Bubble for conversation details
 */
@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val isCustomer = message.role == "customer"
    val alignment = if (isCustomer) Alignment.Start else Alignment.End
    val bgColor = if (isCustomer) BgCard else PrimaryContainerColor
    val textColor = if (isCustomer) OnSurfaceColor else OnPrimaryContainerColor

    // The corner closest to the bubble tail is squared (4dp vs 16dp)
    val bubbleShape = RoundedCornerShape(
        topStart = if (isCustomer) 4.dp else 16.dp,
        topEnd = if (isCustomer) 16.dp else 4.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Surface(
            color = bgColor,
            shape = bubbleShape,
            border = if (isCustomer) BorderStroke(1.dp, OutlineVariantColor) else null,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Time and sender tag
        val tagText = if (!isCustomer) " • Sent by AI" else ""
        Text(
            text = "${message.timestamp.substringAfter("T").substring(0, 5)}$tagText",
            style = MaterialTheme.typography.labelMedium,
            color = OnSurfaceVariantColor,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }
}

/**
 * 9. Activity Feed Item
 */
@Composable
fun ActivityFeedItem(
    lead: LeadEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Simpler compact version card
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("activity_feed_item_${lead.id}"),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, OutlineVariantColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BgHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lead.initials,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp),
                        color = OnSurfaceColor
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1.0f)) {
                    Text(
                        text = lead.customer,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp),
                        color = OnSurfaceColor
                    )
                    Text(
                        text = lead.source,
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceVariantColor
                    )
                }

                // Tiny dynamic status dot on feed
                val dotColor = when (lead.status.lowercase()) {
                    "new" -> StatusNewColor
                    "qualified" -> StatusQualifiedColor
                    "escalated" -> StatusEscalatedColor
                    else -> OnSurfaceVariantColor
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = lead.summary,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = OnSurfaceVariantColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ChannelBadge(channel = lead.channel)
                    StatusBadge(status = lead.status)
                }

                Text(
                    text = TimeHelpers.timeAgo(lead.receivedAt),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                    color = OnSurfaceVariantColor
                )
            }
        }
    }
}

/**
 * 10. Empty State
 */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = OnSurfaceVariantColor,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = OnSurfaceColor
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariantColor,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}

// Utility Helper Data Classes
private data class Triple<A, B, C>(val first: A, val second: B, val third: C)
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
