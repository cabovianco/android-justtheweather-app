package com.cabovianco.justtheweather.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Scheme = darkColorScheme(
    surface = Black,
    onSurface = White,
    onSurfaceVariant = Gray,
)

@Composable
fun JustTheWeatherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = Typography,
        content = content
    )
}
