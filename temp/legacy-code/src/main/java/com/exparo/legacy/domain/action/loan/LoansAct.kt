package com.exparo.wallet.domain.action.loan

import com.exparo.data.db.dao.read.LoanDao
import com.exparo.frp.action.FPAction
import com.exparo.frp.action.thenMap
import com.exparo.frp.then
import com.exparo.legacy.datamodel.Loan
import com.exparo.legacy.datamodel.temp.toLegacyDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class LoansAct @Inject constructor(
    private val loanDao: LoanDao
) : FPAction<Unit, ImmutableList<Loan>>() {
    override suspend fun Unit.compose(): suspend () -> ImmutableList<Loan> = suspend {
        loanDao.findAll()
    } thenMap { it.toLegacyDomain() } then { it.toImmutableList() }
}
