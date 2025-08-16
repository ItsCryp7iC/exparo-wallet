package com.exparo.design.l0_system

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.exparo.base.legacy.Theme
import com.exparo.design.api.ExparoDesign
import com.exparo.design.system.ExparoMaterial3Theme

@Deprecated("Old design system. Use `:exparo-design` and Material3")
val LocalExparoColors = compositionLocalOf<ExparoColors> { error("No ExparoColors") }

@Deprecated("Old design system. Use `:exparo-design` and Material3")
val LocalExparoTypography = compositionLocalOf<ExparoTypography> { error("No ExparoTypography") }

@Deprecated("Old design system. Use `:exparo-design` and Material3")
val LocalExparoShapes = compositionLocalOf<ExparoShapes> { error("No ExparoShapes") }

@Deprecated("Old design system. Use `:exparo-design` and Material3")
object UI {
    val colors: ExparoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExparoColors.current

    val typo: ExparoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalExparoTypography.current

    val shapes: ExparoShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalExparoShapes.current
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoTheme(
    theme: Theme,
    design: ExparoDesign,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = design.colors(theme, isDarkTheme)
    val typography = design.typography()
    val shapes = design.shapes()

    CompositionLocalProvider(
        LocalExparoColors provides colors,
        LocalExparoTypography provides typography,
        LocalExparoShapes provides shapes
    ) {
        val view = LocalView.current
        if (!view.isInEditMode && view.context is Activity) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = Color.Transparent.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    colors.isLight
            }
        }

        ExparoMaterial3Theme(
            dark = !colors.isLight,
            isTrueBlack = theme == Theme.AMOLED_DARK,
            content = content,
        )
    }
}
