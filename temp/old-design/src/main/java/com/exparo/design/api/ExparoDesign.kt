package com.exparo.design.api

import com.exparo.base.legacy.Theme
import com.exparo.design.ExparoContext
import com.exparo.design.l0_system.ExparoColors
import com.exparo.design.l0_system.ExparoShapes
import com.exparo.design.l0_system.ExparoTypography

@Deprecated("Old design system. Use `:exparo-design` and Material3")
interface ExparoDesign {
    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    fun context(): ExparoContext

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    fun typography(): ExparoTypography

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    fun colors(theme: Theme, isDarkModeEnabled: Boolean): ExparoColors

    @Deprecated("Old design system. Use `:exparo-design` and Material3")
    fun shapes(): ExparoShapes
}
