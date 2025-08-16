package com.exparo.design

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.exparo.base.legacy.Theme

@Deprecated("Legacy code. Don't use it, please.")
abstract class ExparoContext {
    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    var theme: Theme by mutableStateOf(Theme.LIGHT)
        private set

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    var screenWidth: Int = -1
        get() {
            return if (field > 0) field else throw IllegalStateException("screenWidth not initialized")
        }

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    var screenHeight: Int = -1
        get() {
            return if (field > 0) field else throw IllegalStateException("screenHeight not initialized")
        }

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    fun switchTheme(theme: Theme) {
        this.theme = theme
    }
}
