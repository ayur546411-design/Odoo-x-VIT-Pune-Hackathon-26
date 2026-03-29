package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AuthColors
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    email: String,
    onResetSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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
                    text = "Reset Password",
                    color = AuthColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

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
                text = "Create New Password",
                color = AuthColors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your new password must be at least\n6 characters long.",
                color = AuthColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // New Password
            Text(
                text = "NEW PASSWORD",
                color = AuthColors.TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it; errorMessage = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("••••••••", color = AuthColors.TextTertiary) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle",
                            tint = AuthColors.TextSecondary
                        )
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

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password
            Text(
                text = "CONFIRM PASSWORD",
                color = AuthColors.TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("••••••••", color = AuthColors.TextTertiary) },
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            imageVector = if (confirmVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle",
                            tint = AuthColors.TextSecondary
                        )
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

            // Error
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = AuthColors.PulseActive,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Reset Button
            Button(
                onClick = {
                    errorMessage = null

                    if (newPassword.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }

                    isLoading = true
                    val result = AuthManager.resetPassword(context, email, newPassword)
                    isLoading = false

                    if (result.success) {
                        onResetSuccess()
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
                    Text("Reset Password", color = AuthColors.Background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
