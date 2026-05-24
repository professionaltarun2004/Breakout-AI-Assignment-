package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

object GeminiHelper {
    private const val TAG = "GeminiHelper"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // Resilient raw response classes
    private class GeminiRequest(
        val contents: List<Content>,
        val systemInstruction: Content? = null
    )
    private class Content(val parts: List<Part>)
    private class Part(val text: String)

    /**
     * Call the Gemini-3.5-Flash API to generate a personalized draft reply based on conversation history.
     */
    suspend fun generateDraftResponse(
        customerName: String,
        channel: String,
        sopMatch: String,
        messagesJson: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API Key is placeholder/empty")
            return@withContext "Error: Gemini API Key is not configured. Please add your real key to the Secrets panel or .env file."
        }

        val prompt = """
            You are drafting a professional customer service response for Closira CRM.
            Customer Name: $customerName
            Communication Channel: $channel
            Active SOP Playbook Match: $sopMatch
            
            Conversation Message Thread (JSON):
            $messagesJson
            
            Based on the prompt above, generate an exceptionally professional, personalized, and persuasive draft reply. 
            Keep it actionable, empathetic, and aligned with the SOP playbook. 
            Avoid code blocks, markdown wrapper quotes, or introductory preamble (such as "Here is your response"). Just output the raw message draft itself.
        """.trimIndent()

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val requestBodyJson = """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": ${escapeJsonText(prompt)}
                    }
                  ]
                }
              ],
              "systemInstruction": {
                "parts": [
                  {
                    "text": "You are a professional customer interaction assistant for Closira CRM. Produce concise, natural, highly persuasive draft responses tailored to the matches."
                  }
                ]
              }
            }
        """.trimIndent()

        try {
            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    Log.e(TAG, "Gemini call failed with response code ${response.code}: $bodyString")
                    return@withContext "Failed to generate draft. Gemini API Error (${response.code})."
                }

                // Resilient JSON Extraction
                val textResponse = parseGeminiTextResponse(bodyString)
                if (textResponse != null) {
                    textResponse.trim()
                } else {
                    "Unable to parse response from Gemini API. Please retry."
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini content generation", e)
            "Error: ${e.localizedMessage ?: "Connection failure. Please check your network connection."}"
        }
    }

    private fun escapeJsonText(text: String): String {
        val escaped = text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n")
            .replace("\t", "\\t")
        return "\"$escaped\""
    }

    private fun parseGeminiTextResponse(jsonString: String): String? {
        // Highly resilient manual parsing of {"candidates":[{"content":{"parts":[{"text":"MESSAGE"}]}}]}
        // to avoid complex generic reflections or model inconsistencies
        return try {
            val candidatesIndex = jsonString.indexOf("\"candidates\"")
            val contentIndex = jsonString.indexOf("\"content\"", candidatesIndex)
            val partsIndex = jsonString.indexOf("\"parts\"", contentIndex)
            val textStartIndex = jsonString.indexOf("\"text\"", partsIndex)
            
            if (textStartIndex != -1) {
                val colonIndex = jsonString.indexOf(":", textStartIndex)
                val firstQuote = jsonString.indexOf("\"", colonIndex)
                var lastQuote = -1
                var escaped = false
                
                for (i in (firstQuote + 1) until jsonString.length) {
                    val char = jsonString[i]
                    if (escaped) {
                        escaped = false
                        continue
                    }
                    if (char == '\\') {
                        escaped = true
                    } else if (char == '"') {
                        lastQuote = i
                        break
                    }
                }
                
                if (firstQuote != -1 && lastQuote != -1) {
                    val rawText = jsonString.substring(firstQuote + 1, lastQuote)
                    return unescapeJsonText(rawText)
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed manual parse fallback", e)
            null
        }
    }

    private fun unescapeJsonText(escaped: String): String {
        return escaped
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
    }
}
