package com.exparo.home

import androidx.compose.runtime.Immutable
import com.exparo.base.legacy.Theme
import com.exparo.base.legacy.TransactionHistoryItem
import com.exparo.home.customerjourney.CustomerJourneyCardModel
import com.exparo.legacy.data.AppBaseData
import com.exparo.legacy.data.BufferInfo
import com.exparo.legacy.data.LegacyDueSection
import com.exparo.legacy.data.model.TimePeriod
import com.exparo.wallet.domain.pure.data.IncomeExpensePair
import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal

@Immutable
data class HomeState(
    val theme: Theme,
    val name: String,

    val period: TimePeriod,
    val baseData: AppBaseData,

    val history: ImmutableList<TransactionHistoryItem>,
    val stats: IncomeExpensePair,

    val balance: BigDecimal,

    val buffer: BufferInfo,

    val upcoming: LegacyDueSection,
    val overdue: LegacyDueSection,

    val customerJourneyCards: ImmutableList<CustomerJourneyCardModel>,
    val hideBalance: Boolean,
    val hideIncome: Boolean,
    val expanded: Boolean,
    val shouldShowAccountSpecificColorInTransactions: Boolean
)
