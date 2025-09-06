package com.exparo.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exparo.accounts.AccountsTab
import com.exparo.base.model.TransactionType
import com.exparo.home.HomeTab
import com.exparo.legacy.ExparoWalletPreview
import com.exparo.legacy.data.model.MainTab
import com.exparo.legacy.exparoWalletCtx
import com.exparo.legacy.utils.onScreenStart
import com.exparo.navigation.EditPlannedScreen
import com.exparo.navigation.EditTransactionScreen
import com.exparo.navigation.MainScreen
import com.exparo.navigation.navigation
import com.exparo.ui.components.CalculatorModal
import com.exparo.ui.components.FloatingCalculatorButton
import com.exparo.wallet.domain.deprecated.logic.model.CreateAccountData
import com.exparo.wallet.ui.theme.modal.edit.AccountModal
import com.exparo.wallet.ui.theme.modal.edit.AccountModalData

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun BoxWithConstraintsScope.MainScreen(screen: MainScreen) {
    val viewModel: MainViewModel = viewModel()

    val currency by viewModel.currency.observeAsState("")

    onScreenStart {
        viewModel.start(screen)
    }

    val exparoContext = exparoWalletCtx()
    UI(
        screen = screen,
        tab = exparoContext.mainTab,
        baseCurrency = currency,
        selectTab = viewModel::selectTab,
        onCreateAccount = viewModel::createAccount
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
private fun BoxWithConstraintsScope.UI(
    screen: MainScreen,
    tab: MainTab,

    baseCurrency: String,

    selectTab: (MainTab) -> Unit,
    onCreateAccount: (CreateAccountData) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        when (tab) {
            MainTab.HOME -> HomeTab()
            MainTab.ACCOUNTS -> AccountsTab()
        }

        var accountModalData: AccountModalData? by remember { mutableStateOf(null) }
        var isCalculatorVisible by remember { mutableStateOf(false) }

        val nav = navigation()
        BottomBar(
            tab = tab,
            selectTab = selectTab,

            onAddIncome = {
                nav.navigateTo(
                    EditTransactionScreen(
                        initialTransactionId = null,
                        type = TransactionType.INCOME
                    )
                )
            },
            onAddExpense = {
                nav.navigateTo(
                    EditTransactionScreen(
                        initialTransactionId = null,
                        type = TransactionType.EXPENSE
                    )
                )
            },
            onAddTransfer = {
                nav.navigateTo(
                    EditTransactionScreen(
                        initialTransactionId = null,
                        type = TransactionType.TRANSFER
                    )
                )
            },
            onAddPlannedPayment = {
                nav.navigateTo(
                    EditPlannedScreen(
                        type = TransactionType.EXPENSE,
                        plannedPaymentRuleId = null
                    )
                )
            },

            showAddAccountModal = {
                accountModalData = AccountModalData(
                    account = null,
                    balance = 0.0,
                    baseCurrency = baseCurrency
                )
            }
        )

        AccountModal(
            modal = accountModalData,
            onCreateAccount = onCreateAccount,
            onEditAccount = { _, _ -> },
            dismiss = {
                accountModalData = null
            }
        )

        // Floating Calculator Button
        FloatingCalculatorButton(
            onClick = {
                isCalculatorVisible = true
            },
            modifier = Modifier
                .padding(end = 16.dp, bottom = 120.dp) // Position above bottom bar
                .align(Alignment.BottomEnd)
        )

        // Calculator Modal
        CalculatorModal(
            visible = isCalculatorVisible,
            onDismiss = {
                isCalculatorVisible = false
            }
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview
@Composable
private fun PreviewMainScreen() {
    ExparoWalletPreview {
        UI(
            screen = MainScreen,
            tab = MainTab.HOME,
            baseCurrency = "BGN",
            selectTab = {},
            onCreateAccount = { }
        )
    }
}
