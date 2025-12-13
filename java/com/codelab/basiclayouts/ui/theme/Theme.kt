
package com.codelab.basiclayouts.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = GlassPrimary,
    secondary = GlassSecondary,
    tertiary = GlassTertiary,
    background = GradientStart,
    surface = GlassSurface,
    onPrimary = GlassTextPrimary,
    onSecondary = GlassTextPrimary,
    onTertiary = GlassTextPrimary,
    onBackground = GlassTextPrimary,
    onSurface = GlassTextPrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGlassPrimary,
    secondary = DarkGlassSecondary,
    tertiary = GlassTertiary,
    background = DarkGradientStart,
    surface = GlassSurface,
    onPrimary = GlassTextPrimary,
    onSecondary = GlassTextPrimary,
    onTertiary = GlassTextPrimary,
    onBackground = GlassTextPrimary,
    onSurface = GlassTextPrimary,
)

@Composable
fun MySootheTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}