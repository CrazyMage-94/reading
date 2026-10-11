package com.reading.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green = Color(0xFF1B5E20)
val DarkGreen = Color(0xFF003300)
val Paper = Color(0xFFF5F1E8)
val Night = Color(0xFF121212)

@Composable
fun ReadingAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) darkColorScheme(primary = Color(0xFF81C784), background = Night)
    else lightColorScheme(primary = Green, background = Paper)
    MaterialTheme(colorScheme = colorScheme, content = content)
}