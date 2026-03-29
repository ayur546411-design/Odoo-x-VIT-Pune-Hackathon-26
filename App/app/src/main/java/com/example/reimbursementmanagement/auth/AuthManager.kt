package com.example.reimbursementmanagement.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.security.MessageDigest

/**
 * Production-level authentication manager.
 * Handles credential storage (SHA-256 hashed passwords), session management,
 * OTP generation/verification, and password reset.
 *
 * Uses SharedPreferences for persistence. For a real production app,
 * consider EncryptedSharedPreferences + Android Keystore.
 */
object AuthManager {

    private const val PREFS_NAME = "auth_prefs"
    private const val SESSION_KEY = "logged_in_email"
    private const val OTP_CODE_PREFIX = "otp_code_"
    private const val OTP_EXPIRY_PREFIX = "otp_expiry_"
    private const val USER_NAME_PREFIX = "user_name_"
    private const val USER_PASS_PREFIX = "user_pass_"
    private const val USER_COUNTRY_PREFIX = "user_country_"
    private const val PREF_NOTIF_PUSH = "notif_push_"
    private const val PREF_NOTIF_EMAIL = "notif_email_"
    private const val PREF_NOTIF_MONTHLY = "notif_monthly_"
    private const val OTP_VALIDITY_MS = 5 * 60 * 1000L // 5 minutes

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ─── Hashing ────────────────────────────────────────────────

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    // ─── Registration ───────────────────────────────────────────

    data class AuthResult(
        val success: Boolean,
        val errorMessage: String? = null
    )

    fun isEmailRegistered(context: Context, email: String): Boolean {
        val prefs = getPrefs(context)
        return prefs.contains(USER_PASS_PREFIX + email.lowercase().trim())
    }

    fun register(
        context: Context,
        name: String,
        email: String,
        password: String,
        country: String = ""
    ): AuthResult {
        val normalizedEmail = email.lowercase().trim()

        if (name.isBlank()) return AuthResult(false, "Name is required")
        if (normalizedEmail.isBlank()) return AuthResult(false, "Email is required")
        if (!normalizedEmail.contains("@")) return AuthResult(false, "Invalid email address")
        if (password.length < 6) return AuthResult(false, "Password must be at least 6 characters")

        if (isEmailRegistered(context, normalizedEmail)) {
            return AuthResult(false, "An account with this email already exists")
        }

        val prefs = getPrefs(context)
        prefs.edit().apply {
            putString(USER_NAME_PREFIX + normalizedEmail, name.trim())
            putString(USER_PASS_PREFIX + normalizedEmail, hashPassword(password))
            putString(USER_COUNTRY_PREFIX + normalizedEmail, country.trim())
            // Auto-login after registration
            putString(SESSION_KEY, normalizedEmail)
            apply()
        }

        Log.d("AuthManager", "User registered: $normalizedEmail")
        return AuthResult(true)
    }

    // ─── Login ──────────────────────────────────────────────────

    fun login(context: Context, email: String, password: String): AuthResult {
        val normalizedEmail = email.lowercase().trim()

        if (normalizedEmail.isBlank()) return AuthResult(false, "Email is required")
        if (password.isBlank()) return AuthResult(false, "Password is required")

        val prefs = getPrefs(context)
        val storedHash = prefs.getString(USER_PASS_PREFIX + normalizedEmail, null)
            ?: return AuthResult(false, "No account found with this email")

        val inputHash = hashPassword(password)
        if (storedHash != inputHash) {
            return AuthResult(false, "Incorrect password")
        }

        // Set session
        prefs.edit().putString(SESSION_KEY, normalizedEmail).apply()
        Log.d("AuthManager", "User logged in: $normalizedEmail")
        return AuthResult(true)
    }

    // ─── Session ────────────────────────────────────────────────

    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getString(SESSION_KEY, null) != null
    }

    fun getCurrentUserEmail(context: Context): String? {
        return getPrefs(context).getString(SESSION_KEY, null)
    }

    fun getCurrentUserName(context: Context): String? {
        val email = getCurrentUserEmail(context) ?: return null
        return getPrefs(context).getString(USER_NAME_PREFIX + email, null)
    }

    fun getCurrentUserCountry(context: Context): String? {
        val email = getCurrentUserEmail(context) ?: return null
        return getPrefs(context).getString(USER_COUNTRY_PREFIX + email, null)
    }

    fun updateUserDetails(context: Context, name: String, country: String): AuthResult {
        val email = getCurrentUserEmail(context) ?: return AuthResult(false, "Not logged in")
        
        if (name.isBlank()) return AuthResult(false, "Name cannot be empty")
        
        getPrefs(context).edit().apply {
            putString(USER_NAME_PREFIX + email, name.trim())
            putString(USER_COUNTRY_PREFIX + email, country.trim())
            apply()
        }
        return AuthResult(true)
    }

    fun logout(context: Context) {
        getPrefs(context).edit().remove(SESSION_KEY).apply()
        Log.d("AuthManager", "User logged out")
    }

    // ─── OTP ────────────────────────────────────────────────────

    fun generateOtp(context: Context, email: String): String? {
        val normalizedEmail = email.lowercase().trim()

        if (!isEmailRegistered(context, normalizedEmail)) {
            return null // email not found
        }

        val otp = (1000..9999).random().toString()
        val expiry = System.currentTimeMillis() + OTP_VALIDITY_MS

        getPrefs(context).edit().apply {
            putString(OTP_CODE_PREFIX + normalizedEmail, otp)
            putLong(OTP_EXPIRY_PREFIX + normalizedEmail, expiry)
            apply()
        }

        Log.d("AuthManager", "OTP generated for $normalizedEmail: $otp") // For testing
        return otp
    }

    fun verifyOtp(context: Context, email: String, code: String): AuthResult {
        val normalizedEmail = email.lowercase().trim()
        val prefs = getPrefs(context)

        val storedOtp = prefs.getString(OTP_CODE_PREFIX + normalizedEmail, null)
            ?: return AuthResult(false, "No OTP found. Please request a new code.")

        val expiry = prefs.getLong(OTP_EXPIRY_PREFIX + normalizedEmail, 0L)
        if (System.currentTimeMillis() > expiry) {
            // Clear expired OTP
            prefs.edit()
                .remove(OTP_CODE_PREFIX + normalizedEmail)
                .remove(OTP_EXPIRY_PREFIX + normalizedEmail)
                .apply()
            return AuthResult(false, "OTP has expired. Please request a new code.")
        }

        if (storedOtp != code) {
            return AuthResult(false, "Invalid verification code")
        }

        // OTP verified — clear it
        prefs.edit()
            .remove(OTP_CODE_PREFIX + normalizedEmail)
            .remove(OTP_EXPIRY_PREFIX + normalizedEmail)
            .apply()

        Log.d("AuthManager", "OTP verified for $normalizedEmail")
        return AuthResult(true)
    }

    // ─── Password Reset ─────────────────────────────────────────

    fun resetPassword(context: Context, email: String, newPassword: String): AuthResult {
        val normalizedEmail = email.lowercase().trim()

        if (newPassword.length < 6) {
            return AuthResult(false, "Password must be at least 6 characters")
        }

        if (!isEmailRegistered(context, normalizedEmail)) {
            return AuthResult(false, "No account found with this email")
        }

        getPrefs(context).edit()
            .putString(USER_PASS_PREFIX + normalizedEmail, hashPassword(newPassword))
            .apply()

        Log.d("AuthManager", "Password reset for $normalizedEmail")
        return AuthResult(true)
    }

    fun changePassword(context: Context, oldPassword: String, newPassword: String): AuthResult {
        val email = getCurrentUserEmail(context) ?: return AuthResult(false, "Not logged in")
        val prefs = getPrefs(context)
        
        val storedHash = prefs.getString(USER_PASS_PREFIX + email, null)
            ?: return AuthResult(false, "No account found")

        if (storedHash != hashPassword(oldPassword)) {
            return AuthResult(false, "Incorrect current password")
        }

        if (newPassword.length < 6) {
            return AuthResult(false, "New password must be at least 6 characters")
        }

        prefs.edit()
            .putString(USER_PASS_PREFIX + email, hashPassword(newPassword))
            .apply()

        return AuthResult(true)
    }

    // ─── Notifications ──────────────────────────────────────────

    data class NotificationPrefs(
        val pushEnabled: Boolean,
        val emailEnabled: Boolean,
        val monthlyEnabled: Boolean
    )

    fun getNotificationPrefs(context: Context): NotificationPrefs {
        val email = getCurrentUserEmail(context) ?: return NotificationPrefs(true, true, true)
        val prefs = getPrefs(context)
        return NotificationPrefs(
            pushEnabled = prefs.getBoolean(PREF_NOTIF_PUSH + email, true),
            emailEnabled = prefs.getBoolean(PREF_NOTIF_EMAIL + email, true),
            monthlyEnabled = prefs.getBoolean(PREF_NOTIF_MONTHLY + email, true)
        )
    }

    fun updateNotificationPrefs(context: Context, prefsToSave: NotificationPrefs) {
        val email = getCurrentUserEmail(context) ?: return
        getPrefs(context).edit().apply {
            putBoolean(PREF_NOTIF_PUSH + email, prefsToSave.pushEnabled)
            putBoolean(PREF_NOTIF_EMAIL + email, prefsToSave.emailEnabled)
            putBoolean(PREF_NOTIF_MONTHLY + email, prefsToSave.monthlyEnabled)
            apply()
        }
    }
}
