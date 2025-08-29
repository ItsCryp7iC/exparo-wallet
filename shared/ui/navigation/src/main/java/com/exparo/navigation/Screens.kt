package com.exparo.navigation

import com.exparo.base.legacy.Transaction
import com.exparo.base.model.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

data object MainScreen : Screen("main") {
    override val isLegacy: Boolean
        get() = true
}

data object OnboardingScreen : Screen("onboarding") {
    override val isLegacy: Boolean
        get() = true
}

data class CSVScreen(
    val launchedFromOnboarding: Boolean
) : Screen("csv") {
    override val isLegacy: Boolean
        get() = true
}

data class EditTransactionScreen(
    val initialTransactionId: UUID?,
    val type: TransactionType,
    // extras
    val accountId: UUID? = null,
    val categoryId: UUID? = null
) : Screen("edit_transaction") {
    override val isLegacy: Boolean
        get() = true
}

data class TransactionsScreen(
    val accountId: UUID? = null,
    val categoryId: UUID? = null,
    val unspecifiedCategory: Boolean? = false,
    val transactionType: TransactionType? = null,
    val accountIdFilterList: List<UUID> = persistentListOf(),
    val transactions: List<Transaction> = persistentListOf()
) : Screen("transactions") {
    override val isLegacy: Boolean
        get() = true
}

data class PieChartStatisticScreen(
    val type: TransactionType,
    val filterExcluded: Boolean = true,
    val accountList: ImmutableList<UUID> = persistentListOf(),
    val transactions: ImmutableList<Transaction> = persistentListOf(),
    val treatTransfersAsIncomeExpense: Boolean = false
) : Screen("pie_chart_statistic") {
    override val isLegacy: Boolean
        get() = true
}

data class EditPlannedScreen(
    val plannedPaymentRuleId: UUID?,
    val type: TransactionType,
    val amount: Double? = null,
    val accountId: UUID? = null,
    val categoryId: UUID? = null,
    val title: String? = null,
    val description: String? = null,
) : Screen("edit_planned") {
    override val isLegacy: Boolean
        get() = true

    fun mandatoryFilled(): Boolean {
        return amount != null && amount > 0.0 &&
                accountId != null
    }
}

data object BalanceScreen : Screen("balance") {
    override val isLegacy: Boolean
        get() = true
}

data object PlannedPaymentsScreen : Screen("planned_payments") {
    override val isLegacy: Boolean
        get() = true
}

data object CategoriesScreen : Screen("categories") {
    override val isLegacy: Boolean
        get() = true
}

data object SettingsScreen : Screen("settings") {
    override val isLegacy: Boolean
        get() = true
}

data class ImportScreen(
    val launchedFromOnboarding: Boolean
) : Screen("import") {
    override val isLegacy: Boolean
        get() = true
}

data object ReportScreen : Screen("report") {
    override val isLegacy: Boolean
        get() = true
}

data object BudgetScreen : Screen("budget") {
    override val isLegacy: Boolean
        get() = true
}

data object LoansScreen : Screen("loans") {
    override val isLegacy: Boolean
        get() = true
}

data object SearchScreen : Screen("search") {
    override val isLegacy: Boolean
        get() = true
}

data class LoanDetailsScreen(
    val loanId: UUID
) : Screen("loan_details") {
    override val isLegacy: Boolean
        get() = true
}

data object ExchangeRatesScreen : Screen("exchange_rates") {
    override val isLegacy: Boolean
        get() = true
}

data object FeaturesScreen : Screen("features")

data object AttributionsScreen : Screen("attributions")

data object ContributorsScreen : Screen("contributors")

data object ReleasesScreen : Screen("releases")

data object DisclaimerScreen : Screen("disclaimer")

data object PollScreen : Screen("poll")

data class DriveBackupScreen(
    val isFromOnboarding: Boolean = false
) : Screen("drive_backup") {
    override val isLegacy: Boolean
        get() = true
}

// This is our new screen, now correctly defined
data object ScheduledBackupSettings : Screen("scheduled_backup_settings")