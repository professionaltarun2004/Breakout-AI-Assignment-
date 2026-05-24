package com.example.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeHelpers {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun timeAgo(isoString: String?): String {
        if (isoString.isNullOrEmpty()) return "Just now"
        return try {
            val date = isoFormat.parse(isoString) ?: return "Just now"
            // Set current time to respect mock timestamps in May 2025/2026
            val nowCalendar = Calendar.getInstance()
            val dateCalendar = Calendar.getInstance().apply { time = date }
            
            val diffMs = nowCalendar.timeInMillis - dateCalendar.timeInMillis
            val diffMins = diffMs / (1000 * 60)
            
            when {
                diffMins < 1 -> "Just now"
                diffMins < 60 -> "${diffMins}m ago"
                diffMins < 1440 -> "${diffMins / 60}h ago"
                diffMins < 2880 -> "Yesterday"
                else -> "${diffMins / 1440}d ago"
            }
        } catch (e: Exception) {
            "Just now"
        }
    }

    fun formatDueTime(isoString: String?, isOverdue: Boolean): String {
        if (isoString.isNullOrEmpty()) return "No due time"
        return try {
            val date = isoFormat.parse(isoString) ?: return "No due time"
            val displayFormat = SimpleDateFormat("h:mm a", Locale.US)
            val formattedTime = displayFormat.format(date)
            
            if (isOverdue) {
                "Overdue ($formattedTime)"
            } else {
                "Due at $formattedTime"
            }
        } catch (e: Exception) {
            "Due soon"
        }
    }

    fun formatHeaderDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("EEEE, MMMM d", Locale.US)
        val dateString = format.format(calendar.time)
        
        // Add ordinal suffix (st, nd, rd, th)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val suffix = when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
        return "Today, $dateString$suffix"
    }
}
