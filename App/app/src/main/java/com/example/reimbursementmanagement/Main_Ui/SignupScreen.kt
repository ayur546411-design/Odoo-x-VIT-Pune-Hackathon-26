package com.example.reimbursementmanagement.Main_Ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reimbursementmanagement.Main_Ui.compo.AuthColors
import com.example.reimbursementmanagement.auth.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    val context = LocalContext.current
    var adminName by remember { mutableStateOf("") }
    var workEmail by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isTermsChecked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AuthColors.Background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CorporateFare,
                        contentDescription = "Logo",
                        tint = AuthColors.TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FINANCIER",
                        color = AuthColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "Log In",
                    color = AuthColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Admin\nAccount",
                    color = AuthColors.TextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Empower your organization with\nprecision financial control.",
                    color = AuthColors.TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Form Fields
                CustomInputField(
                    label = "COMPANY ADMIN NAME",
                    value = adminName,
                    onValueChange = { adminName = it; errorMessage = null },
                    placeholder = "e.g. Alexander Hamilton"
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                CustomInputField(
                    label = "WORK EMAIL",
                    value = workEmail,
                    onValueChange = { workEmail = it; errorMessage = null },
                    placeholder = "admin@company.com",
                    keyboardType = KeyboardType.Email
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                CustomDropdownField(
                    label = "COUNTRY",
                    value = country,
                    placeholder = "Select Region"
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                CustomInputField(
                    label = "PASSWORD",
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    placeholder = "Min. 6 characters",
                    isPassword = true
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                CustomInputField(
                    label = "CONFIRM PASSWORD",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; errorMessage = null },
                    placeholder = "Re-enter password",
                    isPassword = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Terms Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isTermsChecked,
                        onCheckedChange = { isTermsChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AuthColors.TextPrimary,
                            uncheckedColor = AuthColors.TextSecondary,
                            checkmarkColor = AuthColors.Background
                        ),
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = (-4).dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    val termsText = buildAnnotatedString {
                        append("I agree to the ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = AuthColors.TextPrimary)) {
                            append("Terms of Service")
                        }
                        append(" and\nacknowledge the ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = AuthColors.TextPrimary)) {
                            append("Privacy Policy")
                        }
                        append(".")
                    }
                    Text(
                        text = termsText,
                        color = AuthColors.TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }

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

                // Submit Button
                OutlinedButton(
                    onClick = {
                        errorMessage = null

                        // Client-side validation
                        if (adminName.isBlank()) {
                            errorMessage = "Name is required"; return@OutlinedButton
                        }
                        if (workEmail.isBlank() || !workEmail.contains("@")) {
                            errorMessage = "Valid email is required"; return@OutlinedButton
                        }
                        if (password.length < 6) {
                            errorMessage = "Password must be at least 6 characters"; return@OutlinedButton
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"; return@OutlinedButton
                        }
                        if (!isTermsChecked) {
                            errorMessage = "You must agree to the Terms of Service"; return@OutlinedButton
                        }

                        isLoading = true
                        val result = AuthManager.register(
                            context = context,
                            name = adminName,
                            email = workEmail,
                            password = password,
                            country = country
                        )
                        isLoading = false

                        if (result.success) {
                            onSignupSuccess()
                        } else {
                            errorMessage = result.errorMessage
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isLoading,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuthColors.TextPrimary),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = AuthColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Create Company Profile",
                                color = AuthColors.TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Submit",
                                tint = AuthColors.TextPrimary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                val loginText = buildAnnotatedString {
                    append("Already an admin? ")
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold, 
                        color = AuthColors.TextPrimary,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append("Log In here")
                    }
                }
                Text(
                    text = loginText,
                    color = AuthColors.TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(48.dp))
            
            // Footer
            Text(
                text = "© 2024 Digital Inkwell Finance",
                color = AuthColors.TextTertiary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Privacy", color = AuthColors.TextTertiary, fontSize = 12.sp)
                Text("Terms", color = AuthColors.TextTertiary, fontSize = 12.sp)
                Text("Support", color = AuthColors.TextTertiary, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AuthColors.TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            placeholder = {
                Text(placeholder, color = AuthColors.TextTertiary, fontSize = 14.sp)
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    label: String,
    value: String,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AuthColors.TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(AuthColors.InputBackground, RoundedCornerShape(4.dp))
                .border(1.dp, AuthColors.InputBorder, RoundedCornerShape(4.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (value.isEmpty()) placeholder else value,
                    color = if (value.isEmpty()) AuthColors.TextTertiary else AuthColors.TextPrimary,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = AuthColors.TextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
