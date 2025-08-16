package com.exparo.design.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.exparo.design.l1_buildingBlocks.data.ExparoPadding

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun Modifier.exparoPadding(exparoPadding: ExparoPadding): Modifier {
    return this.padding(
        top = exparoPadding.top ?: 0.dp,
        bottom = exparoPadding.bottom ?: 0.dp,
        start = exparoPadding.start ?: 0.dp,
        end = exparoPadding.end ?: 0.dp
    )
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun padding(
    top: Dp? = null,
    start: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null
): ExparoPadding {
    return ExparoPadding(
        top = top,
        bottom = bottom,
        start = start,
        end = end
    )
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun padding(
    horizontal: Dp? = null,
    vertical: Dp? = null
): ExparoPadding {
    return ExparoPadding(
        top = vertical,
        bottom = vertical,
        start = horizontal,
        end = horizontal
    )
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun padding(
    all: Dp? = null
): ExparoPadding {
    return ExparoPadding(
        top = all,
        bottom = all,
        start = all,
        end = all
    )
}
