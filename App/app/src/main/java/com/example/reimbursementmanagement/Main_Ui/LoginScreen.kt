package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AuthColors
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AuthColors.Background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Circle Graphic (Top Right)
            Box(
                modifier = Modifier
                    .offset(x = 100.dp, y = (-20).dp)
                    .size(160.dp)
                    .align(Alignment.TopEnd)
                    .border(2.dp, AuthColors.TopCircleAccent, CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // Form Container
                Column(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Welcome Back",
                        color = AuthColors.TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Please enter your credentials to\ncontinue",
                        color = AuthColors.TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Email Field
                    Text(
                        text = "Email Address",
                        color = AuthColors.TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text("inkwell@financier.com", color = AuthColors.TextTertiary, fontSize = 14.sp)
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

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Password Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Password",
                            color = AuthColors.TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Forgot password?",
                            color = AuthColors.TextPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onNavigateToForgot() }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text("••••••••", color = AuthColors.TextTertiary, fontSize = 14.sp)
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = AuthColors.TextSecondary)
                            }
                        },
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

                    // Error Message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = AuthColors.PulseActive,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    OutlinedButton(
                        onClick = {
                            errorMessage = null

                            if (email.isBlank()) {
                                errorMessage = "Email is required"; return@OutlinedButton
                            }
                            if (password.isBlank()) {
                                errorMessage = "Password is required"; return@OutlinedButton
                            }

                            isLoading = true
                            val result = AuthManager.login(context, email, password)
                            isLoading = false

                            if (result.success) {
                                onLoginSuccess()
                            } else {
                                errorMessage = result.errorMessage
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AuthColors.TextPrimary),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF161616))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = AuthColors.TextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "LOGIN",
                                    color = AuthColors.TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Login",
                                    tint = AuthColors.TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // Signup Link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val signupText = buildAnnotatedString {
                            append("Don't have an account? ")
                            withStyle(style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = AuthColors.TextPrimary,
                                textDecoration = TextDecoration.Underline
                            )) {
                                append("Signup")
                            }
                        }
                        Text(
                            text = signupText,
                            color = AuthColors.TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onNavigateToSignup() }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(64.dp))

                // Footer
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    Text("PRIVACY", color = AuthColors.TextTertiary, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text("•", color = AuthColors.TextTertiary, fontSize = 12.sp)
                    Text("TERMS", color = AuthColors.TextTertiary, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text("•", color = AuthColors.TextTertiary, fontSize = 12.sp)
                    Text("SUPPORT", color = AuthColors.TextTertiary, fontSize = 12.sp, letterSpacing = 1.sp)
                }

                Text(
                    text = "© 2024 Digital Inkwell Finance",
                    color = AuthColors.TextTertiary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Secure",
                        tint = AuthColors.TextTertiary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BANK-GRADE ENCRYPTION ACTIVE",
                        color = AuthColors.TextTertiary,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
