package com.example.reimbursementmanagement.data

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import com.example.reimbursementmanagement.BuildConfig

/**
 * Calls the Gemini API with a bill image to extract structured data.
 * Uses the REST API directly for maximum compatibility.
 */
object GeminiApi {

    // Loaded from local.properties via BuildConfig
    private val API_KEY = BuildConfig.GEMINI_API

    private const val MODEL = "gemini-2.0-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    /**
     * Send a bill image to Gemini and extract structured expense data.
     * Returns an ExtractedBillData object with amount, category, date, and title.
     */
    suspend fun extractBillData(imageBytes: ByteArray): ExtractedBillData = withContext(Dispatchers.IO) {
        val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

        val prompt = """
            Analyze this receipt/bill image and extract the following information.
            Return ONLY a valid JSON object with these exact fields:
            {
              "title": "merchant/vendor name",
              "amount": 0.00,
              "category": "one of: TRAVEL, MEALS, INFRASTRUCTURE, HARDWARE, SUPPLIES, SOFTWARE, OTHER",
              "date": "YYYY-MM-DD format"
            }
            
            Rules:
            - amount must be a number (no currency symbols)
            - category must be exactly one of the listed options
            - date must be in YYYY-MM-DD format
            - If you cannot determine a field, use empty string for text or 0.0 for amount
            - Return ONLY the JSON, no markdown, no explanation
        """.trimIndent()

        val requestBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                        put(JSONObject().apply {
                            put("inlineData", JSONObject().apply {
                                put("mimeType", "image/jpeg")
                                put("data", base64Image)
                            })
                        })
                    })
                })
            })
        }

        val url = URL("$BASE_URL/$MODEL:generateContent?key=$API_KEY")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.connectTimeout = 30000
        connection.readTimeout = 60000

        try {
            connection.outputStream.use { os ->
                os.write(requestBody.toString().toByteArray())
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("GeminiApi", "Response: $response")
                parseGeminiResponse(response)
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                Log.e("GeminiApi", "Error $responseCode: $error")
                ExtractedBillData()
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Convert a Bitmap to JPEG bytes for sending to Gemini.
     */
    fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        return stream.toByteArray()
    }

    private fun parseGeminiResponse(response: String): ExtractedBillData {
        return try {
            val jsonResponse = JSONObject(response)
            val candidates = jsonResponse.getJSONArray("candidates")
            val content = candidates.getJSONObject(0).getJSONObject("content")
            val parts = content.getJSONArray("parts")
            var textContent = parts.getJSONObject(0).getString("text")

            // Clean up — Gemini sometimes wraps JSON in markdown code blocks
            textContent = textContent
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val data = JSONObject(textContent)

            ExtractedBillData(
                title = data.optString("title", ""),
                amount = data.optDouble("amount", 0.0),
                category = data.optString("category", "OTHER"),
                date = data.optString("date", "")
            )
        } catch (e: Exception) {
            Log.e("GeminiApi", "Failed to parse Gemini response", e)
            ExtractedBillData()
        }
    }
}
