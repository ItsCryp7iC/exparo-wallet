package com.exparo.design.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.exparo.base.resource.AndroidResourceProvider
import com.exparo.base.legacy.Theme
import com.exparo.base.time.impl.DeviceTimeProvider
import com.exparo.base.time.impl.StandardTimeConverter
import com.exparo.design.ExparoContext
import com.exparo.design.api.ExparoDesign
import com.exparo.design.api.ExparoUI
import com.exparo.design.api.systems.ExparoWalletDesign
import com.exparo.design.l0_system.UI
import com.exparo.ui.time.impl.AndroidDevicePreferences
import com.exparo.ui.time.impl.ExparoTimeFormatter

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoComponentPreview(
    modifier: Modifier = Modifier,
    design: ExparoDesign = defaultDesign(),
    theme: Theme = Theme.LIGHT,
    content: @Composable BoxScope.() -> Unit
) {
    ExparoPreview(
        design = design,
        theme = theme
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(UI.colors.pure),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoPreview(
    design: ExparoDesign,
    theme: Theme = Theme.LIGHT,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    design.context().switchTheme(theme = theme)
    val timeProvider = DeviceTimeProvider()
    val timeConverter = StandardTimeConverter(timeProvider)
    ExparoUI(
        design = design,
        content = content,
        timeConverter = timeConverter,
        timeProvider = timeProvider,
        timeFormatter = ExparoTimeFormatter(
            resourceProvider = AndroidResourceProvider(LocalContext.current),
            timeProvider = timeProvider,
            converter = timeConverter,
            devicePreferences = AndroidDevicePreferences(LocalContext.current)
        )
    )
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun defaultDesign(): ExparoDesign = object : ExparoWalletDesign() {
    override fun context(): ExparoContext = object : ExparoContext() {
    }
}
