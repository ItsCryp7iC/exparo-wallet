package com.exparo.wallet.domain.action.budget

import com.exparo.data.db.dao.read.BudgetDao
import com.exparo.frp.action.FPAction
import com.exparo.frp.action.thenMap
import com.exparo.frp.then
import com.exparo.legacy.datamodel.Budget
import com.exparo.legacy.datamodel.temp.toLegacyDomain
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class BudgetsAct @Inject constructor(
    private val budgetDao: BudgetDao
) : FPAction<Unit, ImmutableList<Budget>>() {
    override suspend fun Unit.compose(): suspend () -> ImmutableList<Budget> = suspend {
        budgetDao.findAll()
    } thenMap { it.toLegacyDomain() } then { it.toImmutableList() }
}
