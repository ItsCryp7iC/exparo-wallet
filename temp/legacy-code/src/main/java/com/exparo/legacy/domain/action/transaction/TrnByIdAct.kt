package com.exparo.wallet.domain.action.transaction

import com.exparo.base.legacy.Transaction
import com.exparo.data.model.TransactionId
import com.exparo.data.repository.TransactionRepository
import com.exparo.data.repository.mapper.TransactionMapper
import com.exparo.frp.action.FPAction
import com.exparo.frp.then
import com.exparo.legacy.datamodel.temp.toLegacy
import java.util.UUID
import javax.inject.Inject

class TrnByIdAct @Inject constructor(
    private val transactionRepo: TransactionRepository,
    private val mapper: TransactionMapper
) : FPAction<UUID, Transaction?>() {
    override suspend fun UUID.compose(): suspend () -> Transaction? = suspend {
        this // transactionId
    } then {
        transactionRepo.findById(TransactionId(it))
    } then {
        it?.toLegacy(mapper)
    }
}
