package com.exparo.wallet.domain.action.transaction

import com.exparo.data.model.Transaction
import com.exparo.data.repository.TransactionRepository
import com.exparo.frp.action.FPAction
import javax.inject.Inject

class AllTrnsAct @Inject constructor(
    private val transactionRepository: TransactionRepository
) : FPAction<Unit, List<Transaction>>() {
    override suspend fun Unit.compose(): suspend () -> List<Transaction> = suspend {
        transactionRepository.findAll()
    }
}
