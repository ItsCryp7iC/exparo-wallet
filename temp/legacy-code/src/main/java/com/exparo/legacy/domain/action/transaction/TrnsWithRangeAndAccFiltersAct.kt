package com.exparo.wallet.domain.action.transaction

import com.exparo.base.legacy.Transaction
import com.exparo.data.db.dao.read.TransactionDao
import com.exparo.frp.action.FPAction
import com.exparo.frp.action.thenFilter
import com.exparo.legacy.datamodel.temp.toLegacyDomain
import java.util.UUID
import javax.inject.Inject

class TrnsWithRangeAndAccFiltersAct @Inject constructor(
    private val transactionDao: TransactionDao
) : FPAction<TrnsWithRangeAndAccFiltersAct.Input, List<Transaction>>() {

    override suspend fun Input.compose(): suspend () -> List<Transaction> = suspend {
        transactionDao.findAllBetween(range.from(), range.to())
            .map { it.toLegacyDomain() }
    } thenFilter {
        accountIdFilterSet.contains(it.accountId) || accountIdFilterSet.contains(it.toAccountId)
    }

    data class Input(
        val range: com.exparo.legacy.data.model.FromToTimeRange,
        val accountIdFilterSet: Set<UUID>
    )
}
