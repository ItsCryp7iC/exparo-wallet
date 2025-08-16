package com.exparo.piechart

import androidx.compose.runtime.Immutable
import com.exparo.base.legacy.Transaction
import com.exparo.data.model.Category

@Immutable
data class CategoryAmount(
    val category: Category?,
    val amount: Double,
    val associatedTransactions: List<Transaction> = emptyList(),
    val isCategoryUnspecified: Boolean = false
)
