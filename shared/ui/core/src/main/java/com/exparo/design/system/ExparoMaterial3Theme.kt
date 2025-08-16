package com.exparo.design.system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.exparo.design.system.colors.ExparoColors

@Composable
fun ExparoMaterial3Theme(
    isTrueBlack: Boolean,
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (dark) exparoDarkColorScheme(isTrueBlack) else exparoLightColorScheme(),
        content = content,
    )
}

private fun exparoLightColorScheme(): ColorScheme = ColorScheme(
    primary = ExparoColors.PrimaryBlue.primary,
    onPrimary = ExparoColors.White,
    primaryContainer = ExparoColors.PrimaryBlue.light,
    onPrimaryContainer = ExparoColors.White,
    inversePrimary = ExparoColors.PrimaryBlue.dark,
    secondary = ExparoColors.SecondaryGreen.primary,
    onSecondary = ExparoColors.White,
    secondaryContainer = ExparoColors.SecondaryGreen.light,
    onSecondaryContainer = ExparoColors.White,
    tertiary = ExparoColors.SecondaryGreen.primary,
    onTertiary = ExparoColors.White,
    tertiaryContainer = ExparoColors.SecondaryGreen.light,
    onTertiaryContainer = ExparoColors.White,

    error = ExparoColors.Red.primary,
    onError = ExparoColors.White,
    errorContainer = ExparoColors.Red.light,
    onErrorContainer = ExparoColors.White,

    background = ExparoColors.White,
    onBackground = ExparoColors.Black,
    surface = ExparoColors.White,
    onSurface = ExparoColors.Black,
    surfaceVariant = ExparoColors.ExtraLightGray,
    onSurfaceVariant = ExparoColors.Black,
    surfaceTint = ExparoColors.Black,
    inverseSurface = ExparoColors.DarkGray,
    inverseOnSurface = ExparoColors.White,

    outline = ExparoColors.Gray,
    outlineVariant = ExparoColors.DarkGray,
    scrim = ExparoColors.ExtraDarkGray.copy(alpha = 0.8f)
)

private fun exparoDarkColorScheme(isTrueBlack: Boolean): ColorScheme = ColorScheme(
    primary = ExparoColors.PrimaryBlue.primary,
    onPrimary = ExparoColors.White,
    primaryContainer = ExparoColors.PrimaryBlue.light,
    onPrimaryContainer = ExparoColors.White,
    inversePrimary = ExparoColors.PrimaryBlue.dark,
    secondary = ExparoColors.SecondaryGreen.primary,
    onSecondary = ExparoColors.White,
    secondaryContainer = ExparoColors.SecondaryGreen.light,
    onSecondaryContainer = ExparoColors.White,
    tertiary = ExparoColors.SecondaryGreen.primary,
    onTertiary = ExparoColors.White,
    tertiaryContainer = ExparoColors.SecondaryGreen.light,
    onTertiaryContainer = ExparoColors.White,

    error = ExparoColors.Red.primary,
    onError = ExparoColors.White,
    errorContainer = ExparoColors.Red.light,
    onErrorContainer = ExparoColors.White,

    background = if (isTrueBlack) ExparoColors.TrueBlack else ExparoColors.Black,
    onBackground = ExparoColors.White,
    surface = if (isTrueBlack) ExparoColors.TrueBlack else ExparoColors.Black,
    onSurface = ExparoColors.White,
    surfaceVariant = ExparoColors.ExtraDarkGray,
    onSurfaceVariant = ExparoColors.White,
    surfaceTint = ExparoColors.White,
    inverseSurface = ExparoColors.LightGray,
    inverseOnSurface = if (isTrueBlack) ExparoColors.TrueBlack else ExparoColors.Black,

    outline = ExparoColors.Gray,
    outlineVariant = ExparoColors.LightGray,
    scrim = ExparoColors.ExtraLightGray.copy(alpha = 0.8f)
)
