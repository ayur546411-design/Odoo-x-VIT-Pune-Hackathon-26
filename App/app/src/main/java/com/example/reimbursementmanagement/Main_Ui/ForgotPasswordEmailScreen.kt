package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AuthColors
import com.example.reimbursementmanagement.auth.AuthManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordEmailScreen(
    onOtpSent: (String) -> Unit, // passes email to next screen
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AuthColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = AuthColors.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Forgot Password",
                    color = AuthColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Email Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(AuthColors.InputBackground, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = AuthColors.TextPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Reset Your Password",
                color = AuthColors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter the email address associated with\nyour account. We'll send a verification\ncode to reset your password.",
                color = AuthColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Email Input
            Text(
                text = "EMAIL ADDRESS",
                color = AuthColors.TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null; successMessage = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = {
                    Text("Enter your registered email", color = AuthColors.TextTertiary, fontSize = 14.sp)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                textStyle = LocalTextStyle.current.copy(color = AuthColors.TextPrimary),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AuthColors.InputBackground,
                    unfocusedContainerColor = AuthColors.InputBackground,
                    focusedBorderColor = AuthColors.InputBorderFocused,
                    unfocusedBorderColor = AuthColors.InputBorder,
                    cursorColor = AuthColors.TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error / Success Messages
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = AuthColors.PulseActive,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = AuthColors.BrandAccent,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Send Code Button
            Button(
                onClick = {
                    errorMessage = null
                    successMessage = null

                    if (email.isBlank() || !email.contains("@")) {
                        errorMessage = "Please enter a valid email address"
                        return@Button
                    }

                    if (!AuthManager.isEmailRegistered(context, email)) {
                        errorMessage = "No account found with this email"
                        return@Button
                    }

                    isLoading = true
                    val otp = AuthManager.generateOtp(context, email)

                    if (otp != null) {
                        scope.launch {
                            try {
                                BrevoApi.sendOtpEmail(email.lowercase().trim(), otp)
                                successMessage = "Verification code sent!"
                            } catch (e: Exception) {
                                // OTP is still stored locally even if email fails
                                successMessage = "Code generated (check Logcat if email fails)"
                            } finally {
                                isLoading = false
                                onOtpSent(email.lowercase().trim())
                            }
                        }
                    } else {
                        isLoading = false
                        errorMessage = "Failed to generate OTP"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = AuthColors.BrandAccent)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = AuthColors.Background,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        "Send Verification Code",
                        color = AuthColors.Background,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
