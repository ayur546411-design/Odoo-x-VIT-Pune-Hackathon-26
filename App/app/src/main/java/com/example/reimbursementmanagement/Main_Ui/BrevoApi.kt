package com.example.reimbursementmanagement.Main_Ui

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object BrevoApi {
    // TODO: Replace with a secure configuration source, e.g. BuildConfig or encrypted storage.
    private const val API_KEY = ""

    /**
     * Sends a transactional email to a specific recipient.
     * Uses /v3/smtp/email for instant delivery (OTPs, password resets, etc.).
     */
    suspend fun sendTransactionalEmail(
        toEmail: String,
        toName: String = "",
        subject: String,
        htmlContent: String,
        senderName: String = "Financier App",
        senderEmail: String = "noreply@financier.com"
    ): String = withContext(Dispatchers.IO) {
        val url = URL("https://api.brevo.com/v3/smtp/email")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("accept", "application/json")
        connection.setRequestProperty("api-key", API_KEY)
        connection.setRequestProperty("content-type", "application/json")
        connection.doOutput = true
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        val requestBody = JSONObject().apply {
            put("sender", JSONObject().apply {
                put("name", senderName)
                put("email", senderEmail)
            })
            put("to", JSONArray().apply {
                put(JSONObject().apply {
                    put("email", toEmail)
                    if (toName.isNotBlank()) put("name", toName)
                })
            })
            put("subject", subject)
            put("htmlContent", htmlContent)
        }

        try {
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode in 200..299) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("BrevoApi", "Transactional email sent successfully: $response")
                response
            } else {
                val errorStream = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                Log.e("BrevoApi", "Error $responseCode: $errorStream")
                throw Exception("Failed to send email (HTTP $responseCode): $errorStream")
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Sends an OTP code to the user's email address via Brevo transactional email.
     */
    suspend fun sendOtpEmail(toEmail: String, otpCode: String) {
        val htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px; background: #131416; color: #ffffff; border-radius: 12px;">
                <h2 style="margin-bottom: 8px;">Password Recovery</h2>
                <p style="color: #9CA3AF; margin-bottom: 24px;">Use the verification code below to reset your password.</p>
                <div style="background: #1A1A1C; border: 1px solid #333336; border-radius: 8px; padding: 24px; text-align: center; margin-bottom: 24px;">
                    <span style="font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #ffffff;">$otpCode</span>
                </div>
                <p style="color: #6B7280; font-size: 12px;">This code expires in 5 minutes. If you didn't request this, ignore this email.</p>
                <hr style="border-color: #333336; margin: 24px 0;" />
                <p style="color: #6B7280; font-size: 11px; text-align: center;">© 2024 Digital Inkwell Finance — Secure End-to-End Encryption</p>
            </div>
        """.trimIndent()

        sendTransactionalEmail(
            toEmail = toEmail,
            subject = "Your Verification Code — Financier App",
            htmlContent = htmlContent
        )
    }
}
