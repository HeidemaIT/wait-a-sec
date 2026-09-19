package com.learnfromsilence.app.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.learnfromsilence.app.ui.design.DesignTokens

private val LightColors = lightColorScheme(
    primary = DesignTokens.Light.primary,
    onPrimary = DesignTokens.Light.onPrimary,
    secondary = DesignTokens.Light.secondary,
    onSecondary = DesignTokens.Light.onSecondary,
    background = DesignTokens.Light.background,
    onBackground = DesignTokens.Light.onBackground,
    surface = DesignTokens.Light.surface,
    onSurface = DesignTokens.Light.onSurface,
    surfaceVariant = DesignTokens.Light.surfaceVariant,
    onSurfaceVariant = DesignTokens.Light.onSurfaceVariant,
    outline = DesignTokens.Light.outline
)

private val DarkColors = darkColorScheme(
    primary = DesignTokens.Dark.primary,
    onPrimary = DesignTokens.Dark.onPrimary,
    secondary = DesignTokens.Dark.secondary,
    onSecondary = DesignTokens.Dark.onSecondary,
    background = DesignTokens.Dark.background,
    onBackground = DesignTokens.Dark.onBackground,
    surface = DesignTokens.Dark.surface,
    onSurface = DesignTokens.Dark.onSurface,
    surfaceVariant = DesignTokens.Dark.surfaceVariant,
    onSurfaceVariant = DesignTokens.Dark.onSurfaceVariant,
    outline = DesignTokens.Dark.outline
)

private val LearnFromSilenceTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.4.sp
    )
)

@Composable
fun LearnFromSilenceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LearnFromSilenceTypography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}
