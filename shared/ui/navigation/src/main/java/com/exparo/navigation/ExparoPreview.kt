package com.exparo.navigation

import androidx.compose.runtime.Composable
import com.exparo.design.system.ExparoMaterial3Theme

@Composable
fun ExparoPreview(
    dark: Boolean = false,
    content: @Composable () -> Unit,
) {
    NavigationRoot(navigation = Navigation()) {
        ExparoMaterial3Theme(dark = dark, isTrueBlack = false, content = content)
    }
}
