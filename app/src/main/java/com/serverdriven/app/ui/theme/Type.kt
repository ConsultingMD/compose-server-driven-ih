package com.serverdriven.app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h5 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, letterSpacing = 0.sp),
    body2 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.sp),
    h4 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, letterSpacing = 0.sp),

    )