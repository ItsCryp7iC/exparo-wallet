package com.exparo.wallet.domain.action.loan

import com.exparo.data.db.dao.read.LoanDao
import com.exparo.frp.action.FPAction
import com.exparo.legacy.datamodel.Loan
import com.exparo.legacy.datamodel.temp.toLegacyDomain
import java.util.UUID
import javax.inject.Inject

class LoanByIdAct @Inject constructor(
    private val loanDao: LoanDao
) : FPAction<UUID, Loan?>() {
    override suspend fun UUID.compose(): suspend () -> Loan? = suspend {
        loanDao.findById(this)?.toLegacyDomain()
    }
}
