package com.exparo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import com.exparo.attributions.AttributionsScreenImpl
import com.exparo.balance.BalanceScreen
import com.exparo.budgets.BudgetScreen
import com.exparo.categories.CategoriesScreen
import com.exparo.contributors.ContributorsScreenImpl
import com.exparo.disclaimer.DisclaimerScreenImpl
import com.exparo.exchangerates.ExchangeRatesScreen
import com.exparo.features.FeaturesScreenImpl
import com.exparo.importdata.csv.CSVScreen
import com.exparo.importdata.csvimport.ImportCSVScreen
import com.exparo.loans.loan.LoansScreen
import com.exparo.loans.loandetails.LoanDetailsScreen
import com.exparo.main.MainScreen
import com.exparo.navigation.AttributionsScreen
import com.exparo.navigation.BalanceScreen
import com.exparo.navigation.BudgetScreen
import com.exparo.navigation.CSVScreen
import com.exparo.navigation.CategoriesScreen
import com.exparo.navigation.ContributorsScreen
import com.exparo.navigation.DisclaimerScreen
import com.exparo.navigation.EditPlannedScreen
import com.exparo.navigation.EditTransactionScreen
import com.exparo.navigation.ExchangeRatesScreen
import com.exparo.navigation.FeaturesScreen
import com.exparo.navigation.ImportScreen
import com.exparo.navigation.LoanDetailsScreen
import com.exparo.navigation.LoansScreen
import com.exparo.navigation.MainScreen
import com.exparo.navigation.OnboardingScreen
import com.exparo.navigation.PieChartStatisticScreen
import com.exparo.navigation.PlannedPaymentsScreen
import com.exparo.navigation.PollScreen
import com.exparo.navigation.ReleasesScreen
import com.exparo.navigation.ReportScreen
import com.exparo.navigation.Screen
import com.exparo.navigation.SearchScreen
import com.exparo.navigation.SettingsScreen
import com.exparo.navigation.TransactionsScreen
import com.exparo.onboarding.OnboardingScreen
import com.exparo.piechart.PieChartStatisticScreen
import com.exparo.planned.edit.EditPlannedScreen
import com.exparo.planned.list.PlannedPaymentsScreen
import com.exparo.poll.impl.ui.PollScreen
import com.exparo.releases.ReleasesScreenImpl
import com.exparo.reports.ReportScreen
import com.exparo.search.SearchScreen
import com.exparo.settings.SettingsScreen
import com.exparo.transaction.EditTransactionScreen
import com.exparo.transactions.TransactionsScreen

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
@Suppress("CyclomaticComplexity", "FunctionNaming")
fun BoxWithConstraintsScope.ExparoNavGraph(screen: Screen?) {
    when (screen) {
        null -> {
            // show nothing
        }

        is MainScreen -> MainScreen(screen = screen)
        is OnboardingScreen -> OnboardingScreen(screen = screen)
        is ExchangeRatesScreen -> ExchangeRatesScreen()
        is EditTransactionScreen -> EditTransactionScreen(screen = screen)
        is TransactionsScreen -> TransactionsScreen(screen = screen)
        is PieChartStatisticScreen -> PieChartStatisticScreen(screen = screen)
        is CategoriesScreen -> CategoriesScreen(screen = screen)
        is SettingsScreen -> SettingsScreen()
        is PlannedPaymentsScreen -> PlannedPaymentsScreen(screen = screen)
        is EditPlannedScreen -> EditPlannedScreen(screen = screen)
        is BalanceScreen -> BalanceScreen(screen = screen)
        is ImportScreen -> ImportCSVScreen(screen = screen)
        is ReportScreen -> ReportScreen(screen = screen)
        is BudgetScreen -> BudgetScreen(screen = screen)
        is LoansScreen -> LoansScreen(screen = screen)
        is LoanDetailsScreen -> LoanDetailsScreen(screen = screen)
        is SearchScreen -> SearchScreen(screen = screen)
        is CSVScreen -> CSVScreen(screen = screen)
        FeaturesScreen -> FeaturesScreenImpl()
        AttributionsScreen -> AttributionsScreenImpl()
        ContributorsScreen -> ContributorsScreenImpl()
        ReleasesScreen -> ReleasesScreenImpl()
        DisclaimerScreen -> DisclaimerScreenImpl()
        PollScreen -> PollScreen()
    }
}
