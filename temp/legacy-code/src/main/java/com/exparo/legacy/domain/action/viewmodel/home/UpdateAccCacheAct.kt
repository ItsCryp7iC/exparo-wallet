package com.exparo.wallet.domain.action.viewmodel.home

import com.exparo.frp.action.FPAction
import com.exparo.legacy.ExparoWalletCtx
import com.exparo.legacy.datamodel.Account
import javax.inject.Inject

class UpdateAccCacheAct @Inject constructor(
    private val exparoWalletCtx: ExparoWalletCtx
) : FPAction<List<Account>, List<Account>>() {
    override suspend fun List<Account>.compose(): suspend () -> List<Account> = suspend {
        val accounts = this

        exparoWalletCtx.accountMap.clear()
        exparoWalletCtx.accountMap.putAll(accounts.map { it.id to it })

        accounts
    }
}
