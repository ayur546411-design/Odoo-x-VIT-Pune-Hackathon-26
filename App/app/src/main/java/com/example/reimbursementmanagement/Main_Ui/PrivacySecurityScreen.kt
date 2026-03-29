package com.example.reimbursementmanagement.Main_Ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AppColors
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySecurityScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = { Text("Privacy & Security", fontWeight = FontWeight.SemiBold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Background,
                    titleContentColor = AppColors.TextPrimary,
                    navigationIconContentColor = AppColors.TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Keep your account secure by updating your password regularly. The new password must be at least 6 characters.",
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Current Password
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it; errorMessage = null },
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showCurrent) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showCurrent = !showCurrent }) {
                        Icon(
                            if (showCurrent) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Visibility",
                            tint = AppColors.TextTertiary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryNavy,
                    unfocusedBorderColor = AppColors.InputBorder
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // New Password
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it; errorMessage = null },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showNew = !showNew }) {
                        Icon(
                            if (showNew) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle Visibility",
                            tint = AppColors.TextTertiary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryNavy,
                    unfocusedBorderColor = AppColors.InputBorder
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.PrimaryNavy,
                    unfocusedBorderColor = AppColors.InputBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = AppColors.PillRejectedText,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Update Password Button
            Button(
                onClick = {
                    errorMessage = null
                    
                    if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                        errorMessage = "All fields are required"
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = "New passwords do not match"
                        return@Button
                    }
                    if (newPassword.length < 6) {
                        errorMessage = "New password must be at least 6 characters"
                        return@Button
                    }

                    isLoading = true
                    val result = AuthManager.changePassword(context, currentPassword, newPassword)
                    isLoading = false

                    if (result.success) {
                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        onBack()
                    } else {
                        errorMessage = result.errorMessage
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryNavy)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = AppColors.Background, modifier = Modifier.size(24.dp))
                } else {
                    Text("Update Password", color = AppColors.Background, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
