package com.exparo.wallet.domain.action.viewmodel.home

import com.exparo.data.model.Category
import com.exparo.frp.action.FPAction
import com.exparo.legacy.ExparoWalletCtx
import javax.inject.Inject

class UpdateCategoriesCacheAct @Inject constructor(
    private val exparoWalletCtx: ExparoWalletCtx
) : FPAction<List<Category>, List<Category>>() {
    override suspend fun List<Category>.compose(): suspend () -> List<Category> = suspend {
        val categories = this

        exparoWalletCtx.categoryMap.clear()
        exparoWalletCtx.categoryMap.putAll(categories.map { it.id.value to it })

        categories
    }
}
