package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Messages inside a single Lead conversation
data class Message(
    val id: String,
    val role: String, // "customer" or "agent"
    val content: String,
    val timestamp: String
)

// Timeline steps for the detailed timeline view
data class TimelineStep(
    val step: String, // "Received", "Qualified", "Escalated", "Resolved"
    val time: String?,
    val done: Boolean,
    val active: Boolean = false
)

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey val id: String,
    val customer: String,
    val initials: String,
    val channel: String, // whatsapp | email | call
    val status: String,   // new | qualified | escalated
    val source: String,
    val receivedAt: String,
    val summary: String,
    val sopMatch: String,
    val aiSummary: String,
    val messagesStr: String, // JSON serialization of List<Message>
    val timelineStr: String  // JSON serialization of List<TimelineStep>
)

@Entity(tableName = "escalations")
data class EscalationEntity(
    @PrimaryKey val id: String,
    val customer: String,
    val initials: String,
    val channel: String,
    val reason: String,
    val urgency: String, // high | medium
    val receivedAt: String,
    val sopMatch: String
)

@Entity(tableName = "followups")
data class FollowUpEntity(
    @PrimaryKey val id: String,
    val customer: String,
    val initials: String,
    val channel: String,
    val dueAt: String,
    val messagePreview: String,
    val done: Boolean,
    val overdue: Boolean = false
)
