package com.exparo.design.utils

import androidx.compose.animation.core.spring

@Deprecated("Old design system. Use `:exparo-design` and Material3")
fun <T> springBounce(
    stiffness: Float = 500f,
) = spring<T>(
    dampingRatio = 0.75f,
    stiffness = stiffness,
)
