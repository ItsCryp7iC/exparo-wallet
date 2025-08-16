package com.exparo.legacy.data.model

import androidx.compose.runtime.Immutable
import com.exparo.legacy.datamodel.Account

@Immutable
data class AccountBalance(
    val account: Account,
    val balance: Double
)
