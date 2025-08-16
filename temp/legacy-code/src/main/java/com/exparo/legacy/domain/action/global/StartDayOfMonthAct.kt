package com.exparo.wallet.domain.action.global

import com.exparo.frp.action.FPAction
import com.exparo.frp.then
import com.exparo.legacy.ExparoWalletCtx
import com.exparo.base.legacy.SharedPrefs
import javax.inject.Inject

class StartDayOfMonthAct @Inject constructor(
    private val sharedPrefs: SharedPrefs,
    private val exparoWalletCtx: ExparoWalletCtx
) : FPAction<Unit, Int>() {

    override suspend fun Unit.compose(): suspend () -> Int = suspend {
        sharedPrefs.getInt(SharedPrefs.START_DATE_OF_MONTH, 1)
    } then { startDay ->
        exparoWalletCtx.setStartDayOfMonth(startDay)
        startDay
    }
}
