package com.cabovianco.justtheweather.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cabovianco.justtheweather.R

private val jetBrainsMono = FontFamily(Font(R.font.jet_brains_mono))
private val bitCountSingle = FontFamily(Font(R.font.bit_count_single))

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = bitCountSingle,
        fontWeight = FontWeight.Normal,
        fontSize = 128.sp,
        lineHeight = 128.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = jetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = jetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = jetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
