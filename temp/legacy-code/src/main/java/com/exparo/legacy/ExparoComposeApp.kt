package com.exparo.legacy

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.exparo.base.legacy.Theme
import com.exparo.base.legacy.appContext
import com.exparo.design.ExparoContext
import com.exparo.design.api.ExparoDesign
import com.exparo.design.api.exparoContext
import com.exparo.design.api.systems.ExparoWalletDesign
import com.exparo.design.l0_system.UI
import com.exparo.design.utils.ExparoPreview
import com.exparo.domain.RootScreen
import com.exparo.navigation.Navigation
import com.exparo.navigation.NavigationRoot

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun exparoWalletCtx(): ExparoWalletCtx = exparoContext() as ExparoWalletCtx

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun rootView(): View = LocalView.current

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun rootActivity(): AppCompatActivity = LocalContext.current as AppCompatActivity

@Composable
fun rootScreen(): RootScreen = LocalContext.current as RootScreen

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoWalletComponentPreview(
    theme: Theme = Theme.LIGHT,
    Content: @Composable BoxScope.() -> Unit
) {
    ExparoWalletPreview(
        theme = theme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(UI.colors.pure),
            contentAlignment = Alignment.Center
        ) {
            Content()
        }
    }
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoWalletPreview(
    theme: Theme = Theme.LIGHT,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    appContext = rootView().context
    ExparoPreview(
        theme = theme,
        design = appDesign(ExparoWalletCtx()),
    ) {
        NavigationRoot(navigation = Navigation()) {
            content()
        }
    }
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun appDesign(context: ExparoWalletCtx): ExparoDesign = object : ExparoWalletDesign() {
    override fun context(): ExparoContext = context
}
