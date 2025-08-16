package com.exparo.legacy.data

import androidx.compose.runtime.Immutable
import com.exparo.data.model.Category
import com.exparo.legacy.datamodel.Account
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AppBaseData(
    val baseCurrency: String,
    val accounts: ImmutableList<Account>,
    val categories: ImmutableList<Category>
)
