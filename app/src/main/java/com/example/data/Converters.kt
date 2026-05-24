package com.example.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromMessageList(messages: List<Message>?): String {
        if (messages == null) return "[]"
        val type = Types.newParameterizedType(List::class.java, Message::class.java)
        val adapter = moshi.adapter<List<Message>>(type)
        return adapter.toJson(messages)
    }

    @TypeConverter
    fun toMessageList(json: String?): List<Message> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, Message::class.java)
        val adapter = moshi.adapter<List<Message>>(type)
        return adapter.fromJson(json) ?: emptyList()
    }

    @TypeConverter
    fun fromTimelineStepList(timeline: List<TimelineStep>?): String {
        if (timeline == null) return "[]"
        val type = Types.newParameterizedType(List::class.java, TimelineStep::class.java)
        val adapter = moshi.adapter<List<TimelineStep>>(type)
        return adapter.toJson(timeline)
    }

    @TypeConverter
    fun toTimelineStepList(json: String?): List<TimelineStep> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, TimelineStep::class.java)
        val adapter = moshi.adapter<List<TimelineStep>>(type)
        return adapter.fromJson(json) ?: emptyList()
    }
}
