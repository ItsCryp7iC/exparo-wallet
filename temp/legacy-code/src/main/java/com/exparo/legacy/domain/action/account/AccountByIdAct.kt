package com.exparo.wallet.domain.action.account

import com.exparo.data.db.dao.read.AccountDao
import com.exparo.frp.action.FPAction
import com.exparo.frp.then
import com.exparo.legacy.datamodel.Account
import com.exparo.legacy.datamodel.temp.toLegacyDomain
import java.util.UUID
import javax.inject.Inject

class AccountByIdAct @Inject constructor(
    private val accountDao: AccountDao
) : FPAction<UUID, Account?>() {
    @Deprecated("Legacy code. Don't use it, please.")
    override suspend fun UUID.compose(): suspend () -> Account? = suspend {
        this // accountId
    } then accountDao::findById then {
        it?.toLegacyDomain()
    }
}
