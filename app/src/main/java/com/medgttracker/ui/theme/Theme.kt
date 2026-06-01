package com.medgttracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MedColorScheme = lightColorScheme(
    primary = Color(0xFF146C94),
    secondary = Color(0xFF19A7CE),
    tertiary = Color(0xFFAFD3E2),
    surface = Color(0xFFF8FBFD),
)

@Composable
fun MedGtTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MedColorScheme, content = content)
}
