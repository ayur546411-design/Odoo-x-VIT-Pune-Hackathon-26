package com.example.reimbursementmanagement.Main_Ui.compo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = AppColors.TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun InputContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppDimens.InputRadius))
            .background(AppColors.CardBackground)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun AmountInput(
    currency: String,
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("AMOUNT")
        InputContainer(modifier = Modifier.height(72.dp)) {
            Row(
                modifier = Modifier
                    .weight(0.3f)
                    .clickable { /* Select Currency */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currency,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Drop Down",
                    tint = AppColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 4.dp),
                color = AppColors.Divider
            )

            Spacer(modifier = Modifier.width(16.dp))

            BasicTextField(
                value = amount,
                onValueChange = onAmountChange,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (amount.isEmpty()) AppColors.AmountText else AppColors.TextPrimary,
                    textAlign = TextAlign.Start
                ),
                singleLine = true,
                modifier = Modifier.weight(0.7f),
                decorationBox = { innerTextField ->
                    if (amount.isEmpty()) {
                        Text(
                            text = "0.00",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.AmountText
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun DropdownInput(
    title: String,
    hint: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title)
        InputContainer(
            modifier = Modifier.clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Category, // Fallback icon
                contentDescription = null,
                tint = AppColors.PrimaryNavyLight,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = hint,
                color = AppColors.TextPrimary,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Drop Down",
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DateInput(
    date: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("DATE")
        InputContainer(
            modifier = Modifier.clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = AppColors.PrimaryNavyLight,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = date,
                color = AppColors.TextPrimary,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = "Calendar",
                tint = AppColors.TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun TextAreaInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("DESCRIPTION")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(AppDimens.InputRadius))
                .background(AppColors.CardBackground)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = AppColors.TextPrimary
                ),
                modifier = Modifier.fillMaxSize(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "Enter purpose of expense...",
                            fontSize = 16.sp,
                            color = AppColors.PlaceholderText
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}
