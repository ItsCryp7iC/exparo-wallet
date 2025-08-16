package com.exparo.piechart

import androidx.compose.runtime.Immutable
import com.exparo.data.model.Category

@Immutable
data class SelectedCategory(
    val category: Category // null - Unspecified
)
