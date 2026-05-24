package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.filled.ContentCopy
import kotlinx.coroutines.launch
import com.example.data.api.GeminiHelper
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.LeadEntity
import com.example.data.Message
import com.example.data.TimelineStep
import com.example.ui.CrmViewModel
import com.example.ui.components.ChannelBadge
import com.example.ui.components.MessageBubble
import com.example.ui.theme.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationDetailScreen(
    viewModel: CrmViewModel,
    leadId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val leadFlow = remember(leadId) { viewModel.getLeadById(leadId) }
    val lead by leadFlow.collectAsStateWithLifecycle(initialValue = null)

    // Moshi helpers for parsing thread and timelines
    val moshi = remember {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    val messagesType = remember {
        Types.newParameterizedType(List::class.java, Message::class.java)
    }
    val timelineType = remember {
        Types.newParameterizedType(List::class.java, TimelineStep::class.java)
    }
    val messagesAdapter = remember { moshi.adapter<List<Message>>(messagesType) }
    val timelineAdapter = remember { moshi.adapter<List<TimelineStep>>(timelineType) }

    if (lead == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgApp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryColor)
        }
        return
    }

    val currentLead = lead!!
    val messages = remember(currentLead.messagesStr) {
        try {
            messagesAdapter.fromJson(currentLead.messagesStr) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    val timeline = remember(currentLead.timelineStr) {
        try {
            timelineAdapter.fromJson(currentLead.timelineStr) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    var isGenerating by remember { mutableStateOf(false) }
    var generatedDraft by remember { mutableStateOf<String?>(null) }
    var copyConfirmed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = BgCard,
                border = BorderStroke(0.5.dp, OutlineVariantColor)
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Visual circular back button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onBack)
                            .testTag("back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Back",
                            tint = OnSurfaceColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = currentLead.customer,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                            color = OnSurfaceColor,
                            maxLines = 1
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Channel: ",
                                style = MaterialTheme.typography.labelMedium,
                                color = OnSurfaceVariantColor
                            )
                            ChannelBadge(channel = currentLead.channel)
                        }
                    }

                    IconButton(onClick = { /* Demo placeholder options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = OnSurfaceVariantColor
                        )
                    }
                }
            }
        },
        containerColor = BgApp,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // AI INSIGHTS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    border = BorderStroke(1.dp, OutlineVariantColor),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SecondaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Insights Summary",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryColor
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentLead.aiSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = OutlineVariantColor, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Rule,
                                contentDescription = null,
                                tint = PrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SOP Playbook Match",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryColor
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentLead.sopMatch,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Green verification tag
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF032B1F),
                            border = BorderStroke(1.dp, SecondaryColor.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "✓ Playbook Executed",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryColor
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔮 LIVE GEMINI DRAFT PLANNER (Proactive Extra AI Feature)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    border = BorderStroke(1.dp, PrimaryColor.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = PrimaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Live Gemini Draft Generator",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryColor
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Drafts a personalized, context-aware reply using the full live conversational history and corresponding SOP directives.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariantColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isGenerating) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(color = PrimaryColor, modifier = Modifier.size(24.dp))
                                    Text(
                                        text = "Querying gemini-3.5-flash with thread history...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariantColor
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = {
                                    isGenerating = true
                                    copyConfirmed = false
                                    scope.launch {
                                        try {
                                            val result = GeminiHelper.generateDraftResponse(
                                                customerName = currentLead.customer,
                                                channel = currentLead.channel,
                                                sopMatch = currentLead.sopMatch,
                                                messagesJson = currentLead.messagesStr
                                            )
                                            generatedDraft = result
                                        } finally {
                                            isGenerating = false
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor,
                                    contentColor = OnPrimaryContainerColor
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("gemini_draft_generator_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Generate Live Custom Draft Reply",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        generatedDraft?.let { draftText ->
                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                color = BgHighest,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, OutlineVariantColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = "GEMINI PROPOSAL DRAFT:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = SecondaryColor
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = draftText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = OnSurfaceColor
                                    )
                                    Divider(
                                        color = OutlineVariantColor,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(draftText))
                                                copyConfirmed = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Copy to Clipboard",
                                                modifier = Modifier.size(16.dp),
                                                tint = SecondaryColor
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (copyConfirmed) "Copied!" else "Copy to Clipboard",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = SecondaryColor
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = OutlineVariantColor, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Security Warning (Strict android_secret_management directive)
                        Text(
                            text = "🔒 Security Warning: The API key for this prototype is bundled within the compiled profile. In production environments, client credentials should only run behind secure proxy servers rather than compiled directly in decompiled APKs.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            ),
                            color = OutlineColor,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CHAT THREAD HEADER
                Text(
                    text = "Conversation Thread",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurfaceColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Message bubbles matching mapped role lists
                if (messages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No messages in this chat.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariantColor
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        messages.forEach { msg ->
                            MessageBubble(message = msg)
                        }
                    }
                }
            }

            // STATIC STATUS TIMELINE FOOTER CARD
            Surface(
                color = BgCard,
                border = BorderStroke(1.dp, OutlineVariantColor),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Lead Progression SOP Status",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = OnSurfaceColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Timeline Layout with lines relative to 4 steps
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        // Drawing proportional line behind step nodes
                        val totalSteps = timeline.size
                        val lastDoneIndex = timeline.indexOfLast { it.done }
                        
                        // Render connection bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .padding(horizontal = 24.dp)
                                .align(Alignment.Center)
                                .background(BgHighest)
                        ) {
                            if (totalSteps > 1 && lastDoneIndex >= 0) {
                                val proportion = lastDoneIndex.toFloat() / (totalSteps - 1).toFloat()
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(proportion)
                                        .background(SecondaryColor)
                                )
                            }
                        }

                        // Render step nodes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            timeline.forEachIndexed { idx, step ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(60.dp)
                                ) {
                                    val (nodeBg, nodeBorder, nodeIconColor) = when {
                                        step.done -> Triple(SecondaryColor, null, OnSecondaryColor)
                                        step.active -> Triple(PrimaryContainerColor, BorderStroke(2.dp, PrimaryColor), OnPrimaryContainerColor)
                                        else -> Triple(BgCard, BorderStroke(1.dp, OutlineColor), OnSurfaceVariantColor)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Circular node
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(nodeBg, CircleShape)
                                            .then(
                                                if (nodeBorder != null) Modifier.border(
                                                    nodeBorder,
                                                    CircleShape
                                                ) else Modifier
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (step.done) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = nodeIconColor,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        } else if (step.active) {
                                            // Glow dot
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(PrimaryColor, CircleShape)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Step Label
                                    Text(
                                        text = step.step,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = if (step.active) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (step.active) PrimaryColor else if (step.done) SecondaryColor else OnSurfaceVariantColor,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
