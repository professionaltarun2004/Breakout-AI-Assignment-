package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ClosiraDarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryContainerColor,
    primaryContainer = PrimaryContainerColor,
    onPrimaryContainer = OnPrimaryContainerColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    background = BgApp,
    onBackground = OnSurfaceColor,
    surface = BgCard,
    onSurface = OnSurfaceColor,
    surfaceVariant = BgElevated,
    onSurfaceVariant = OnSurfaceVariantColor,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor,
    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainerColor
)

@Composable
fun ClosiraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ClosiraDarkColorScheme,
        typography = Typography,
        content = content
    )
}
