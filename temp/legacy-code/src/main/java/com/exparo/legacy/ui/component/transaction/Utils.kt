package com.exparo.legacy.ui.component.transaction

import androidx.compose.runtime.Composable
import com.exparo.data.model.Category
import com.exparo.legacy.datamodel.Account
import com.exparo.legacy.exparoWalletCtx
import java.util.UUID

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun category(
    categoryId: UUID?,
    categories: List<Category>
): Category? {
    val targetId = categoryId ?: return null
    return exparoWalletCtx().categoryMap[targetId] ?: categories.find { it.id.value == targetId }
}

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun account(
    accountId: UUID?,
    accounts: List<Account>
): Account? {
    val targetId = accountId ?: return null
    return exparoWalletCtx().accountMap[targetId] ?: accounts.find { it.id == targetId }
}
