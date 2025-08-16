package com.exparo.balance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.exparo.base.time.TimeConverter
import com.exparo.base.time.TimeProvider
import com.exparo.ui.ComposeViewModel
import com.exparo.legacy.data.model.TimePeriod
import com.exparo.legacy.utils.ioThread
import com.exparo.wallet.domain.action.settings.BaseCurrencyAct
import com.exparo.wallet.domain.action.wallet.CalcWalletBalanceAct
import com.exparo.wallet.domain.deprecated.logic.PlannedPaymentsLogic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val plannedPaymentsLogic: PlannedPaymentsLogic,
    private val exparoContext: com.exparo.legacy.ExparoWalletCtx,
    private val baseCurrencyAct: BaseCurrencyAct,
    private val calcWalletBalanceAct: CalcWalletBalanceAct,
    private val timeProvider: TimeProvider,
    private val timeConverter: TimeConverter,
) : ComposeViewModel<BalanceState, BalanceEvent>() {

    private var period by mutableStateOf(exparoContext.selectedPeriod)
    private var baseCurrencyCode by mutableStateOf("")
    private var currentBalance by mutableDoubleStateOf(0.0)
    private var plannedPaymentsAmount by mutableDoubleStateOf(0.0)
    private var balanceAfterPlannedPayments by mutableDoubleStateOf(0.0)
    private var numberOfMonthsAhead by mutableIntStateOf(1)

    @Composable
    override fun uiState(): BalanceState {
        LaunchedEffect(Unit) {
            start()
        }

        return BalanceState(
            period = period,
            balanceAfterPlannedPayments = balanceAfterPlannedPayments,
            currentBalance = currentBalance,
            baseCurrencyCode = baseCurrencyCode,
            plannedPaymentsAmount = plannedPaymentsAmount
        )
    }

    override fun onEvent(event: BalanceEvent) {
        when (event) {
            is BalanceEvent.OnNextMonth -> nextMonth()
            is BalanceEvent.OnSetPeriod -> setTimePeriod(event.timePeriod)
            is BalanceEvent.OnPreviousMonth -> previousMonth()
        }
    }

    private fun start(
        timePeriod: TimePeriod = exparoContext.selectedPeriod
    ) {
        viewModelScope.launch {
            baseCurrencyCode = baseCurrencyAct(Unit)
            period = timePeriod

            currentBalance = calcWalletBalanceAct(
                CalcWalletBalanceAct.Input(baseCurrencyCode)
            ).toDouble()

            plannedPaymentsAmount = ioThread {
                plannedPaymentsLogic.plannedPaymentsAmountFor(
                    timePeriod.toRange(exparoContext.startDayOfMonth, timeConverter, timeProvider)
                    // + positive if Income > Expenses else - negative
                ) * if (numberOfMonthsAhead >= 0) {
                    numberOfMonthsAhead.toDouble()
                } else {
                    1.0
                }
            }
            balanceAfterPlannedPayments =
                currentBalance + plannedPaymentsAmount
        }
    }

    private fun setTimePeriod(timePeriod: TimePeriod) {
        start(timePeriod = timePeriod)
    }

    private fun nextMonth() {
        val month = period.month
        val year = period.year ?: com.exparo.legacy.utils.dateNowUTC().year
        numberOfMonthsAhead += 1
        if (month != null) {
            start(
                timePeriod = month.incrementMonthPeriod(exparoContext, 1L, year = year)
            )
        }
    }

    private fun previousMonth() {
        val month = period.month
        val year = period.year ?: com.exparo.legacy.utils.dateNowUTC().year
        numberOfMonthsAhead -= 1
        if (month != null) {
            start(
                timePeriod = month.incrementMonthPeriod(exparoContext, -1L, year = year)
            )
        }
    }
}
