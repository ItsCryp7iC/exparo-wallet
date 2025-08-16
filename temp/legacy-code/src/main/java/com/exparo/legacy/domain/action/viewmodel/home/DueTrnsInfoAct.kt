package com.exparo.wallet.domain.action.viewmodel.home

import com.exparo.base.time.TimeProvider
import com.exparo.data.model.Transaction
import com.exparo.frp.action.FPAction
import com.exparo.frp.lambda
import com.exparo.frp.then
import com.exparo.wallet.domain.action.account.AccountByIdAct
import com.exparo.wallet.domain.action.exchange.ExchangeAct
import com.exparo.wallet.domain.action.exchange.actInput
import com.exparo.wallet.domain.action.transaction.DueTrnsAct
import com.exparo.wallet.domain.pure.data.ClosedTimeRange
import com.exparo.wallet.domain.pure.data.IncomeExpensePair
import com.exparo.wallet.domain.pure.exchange.ExchangeTrnArgument
import com.exparo.wallet.domain.pure.exchange.exchangeInBaseCurrency
import com.exparo.wallet.domain.pure.transaction.expenses
import com.exparo.wallet.domain.pure.transaction.incomes
import com.exparo.wallet.domain.pure.transaction.sumTrns
import java.time.LocalDate
import javax.inject.Inject

class DueTrnsInfoAct @Inject constructor(
    private val dueTrnsAct: DueTrnsAct,
    private val accountByIdAct: AccountByIdAct,
    private val exchangeAct: ExchangeAct,
    private val timeProvider: TimeProvider
) : FPAction<DueTrnsInfoAct.Input, DueTrnsInfoAct.Output>() {

    override suspend fun Input.compose(): suspend () -> Output =
        suspend {
            range
        } then dueTrnsAct then { trns ->
            val dateNow = timeProvider.localDateNow()
            trns.filter {
                this.dueFilter(it, dateNow)
            }
        } then { dueTrns ->
            // We have due transactions in different currencies
            val exchangeArg = ExchangeTrnArgument(
                baseCurrency = baseCurrency,
                exchange = ::actInput then exchangeAct,
                getAccount = accountByIdAct.lambda()
            )

            Output(
                dueIncomeExpense = IncomeExpensePair(
                    income = sumTrns(
                        incomes(dueTrns),
                        ::exchangeInBaseCurrency,
                        exchangeArg
                    ),
                    expense = sumTrns(
                        expenses(dueTrns),
                        ::exchangeInBaseCurrency,
                        exchangeArg
                    )
                ),
                dueTrns = dueTrns
            )
        }

    data class Input(
        val range: ClosedTimeRange,
        val baseCurrency: String,
        val dueFilter: (Transaction, LocalDate) -> Boolean
    )

    data class Output(
        val dueIncomeExpense: IncomeExpensePair,
        val dueTrns: List<Transaction>
    )
}
