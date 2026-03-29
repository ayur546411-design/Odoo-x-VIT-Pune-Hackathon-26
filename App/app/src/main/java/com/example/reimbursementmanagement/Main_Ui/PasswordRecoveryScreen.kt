package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AuthColors
import com.example.reimbursementmanagement.auth.AuthManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasswordRecoveryScreen(
    emailAddress: String,
    onVerifySuccess: (String) -> Unit, // passes email to ResetPasswordScreen
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var otpCode by remember { mutableStateOf("") }
    var secondsRemaining by remember { mutableStateOf(59) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Send OTP via Brevo
    fun sendOtp() {
        val otp = AuthManager.generateOtp(context, emailAddress)
        if (otp != null) {
            scope.launch {
                try {
                    BrevoApi.sendOtpEmail(emailAddress, otp)
                } catch (e: Exception) {
                    // OTP is still stored locally — check Logcat
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        sendOtp()
        while (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AuthColors.Background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                        text = "Verification",
                        color = AuthColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Lock Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(AuthColors.InputBackground, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = AuthColors.TextPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Password Recovery",
                    color = AuthColors.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter the 4-digit verification code we sent to\n$emailAddress",
                    color = AuthColors.TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Custom OTP Input
                BasicTextField(
                    value = otpCode,
                    onValueChange = {
                        if (it.length <= 4) {
                            otpCode = it
                            errorMessage = null
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            repeat(4) { index ->
                                val isFocused = otpCode.length == index
                                val char = otpCode.getOrNull(index)
                                
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Color.Transparent, RoundedCornerShape(8.dp))
                                        .border(
                                            width = if (isFocused) 2.dp else 1.dp,
                                            color = if (isFocused) AuthColors.TextPrimary else AuthColors.InputBorder,
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (char != null) {
                                        Text(
                                            text = char.toString(),
                                            color = AuthColors.TextPrimary,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Box(modifier = Modifier
                                            .size(8.dp)
                                            .background(AuthColors.InputBorder, CircleShape))
                                    }
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Error
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = AuthColors.PulseActive,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Didn't receive the code?", color = AuthColors.TextSecondary, fontSize = 14.sp)
                val resendText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = AuthColors.TextPrimary, textDecoration = TextDecoration.Underline)) {
                        append("Resend New Code")
                    }
                }
                Text(
                    text = resendText,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable {
                            sendOtp()
                            secondsRemaining = 59
                            errorMessage = null
                            scope.launch {
                                while (secondsRemaining > 0) {
                                    delay(1000L)
                                    secondsRemaining--
                                }
                            }
                        },
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Verify Button
                Button(
                    onClick = {
                        if (otpCode.length != 4) {
                            errorMessage = "Please enter the complete 4-digit code"
                            return@Button
                        }

                        isLoading = true
                        val result = AuthManager.verifyOtp(context, emailAddress, otpCode)
                        isLoading = false

                        if (result.success) {
                            onVerifySuccess(emailAddress)
                        } else {
                            errorMessage = result.errorMessage
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
                        CircularProgressIndicator(color = AuthColors.Background, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Verify", color = AuthColors.Background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = "Secure", tint = AuthColors.TextTertiary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Secure End-to-End Encryption", color = AuthColors.TextTertiary, fontSize = 12.sp)
                }
            }

            // Bottom Timer Pill
            if (secondsRemaining > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(AuthColors.PillBackground, RoundedCornerShape(24.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(pulseSize.dp)
                                .background(AuthColors.PulseActive, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Waiting for email delivery...", color = AuthColors.TextSecondary, fontSize = 12.sp)
                            Text(
                                text = "00:${secondsRemaining.toString().padStart(2, '0')}",
                                color = AuthColors.TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
