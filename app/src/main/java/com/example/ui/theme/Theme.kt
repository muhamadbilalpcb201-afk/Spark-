package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SparkBluePrimary,
    onPrimary = SparkDarkTextPrimary,
    secondary = SparkCyanNeon,
    onSecondary = SparkDarkTextPrimary,
    tertiary = SparkBlueLight,
    background = SparkDarkBackground,
    onBackground = SparkDarkTextPrimary,
    surface = SparkDarkSurface,
    onSurface = SparkDarkTextPrimary,
    surfaceVariant = SparkDarkSurfaceVariant,
    onSurfaceVariant = SparkDarkTextSecondary,
    outline = SparkDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SparkBluePrimary,
    onPrimary = SparkLightBackground,
    secondary = SparkBlueDark,
    onSecondary = SparkLightBackground,
    tertiary = SparkBlueLight,
    background = SparkLightBackground,
    onBackground = SparkLightTextPrimary,
    surface = SparkLightSurface,
    onSurface = SparkLightTextPrimary,
    surfaceVariant = SparkLightSurfaceVariant,
    onSurfaceVariant = SparkLightTextSecondary,
    outline = SparkLightBorder
)

@Composable
fun SparkTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
