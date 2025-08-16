package com.exparo.search

import com.exparo.base.legacy.TransactionHistoryItem
import com.exparo.data.model.Category
import com.exparo.legacy.datamodel.Account
import kotlinx.collections.immutable.ImmutableList

data class SearchState(
    val searchQuery: String,
    val transactions: ImmutableList<TransactionHistoryItem>,
    val baseCurrency: String,
    val accounts: ImmutableList<Account>,
    val categories: ImmutableList<Category>,
    val shouldShowAccountSpecificColorInTransactions: Boolean
)
