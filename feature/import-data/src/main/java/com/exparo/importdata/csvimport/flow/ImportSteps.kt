package com.exparo.importdata.csvimport.flow

import androidx.compose.runtime.Composable
import com.exparo.importdata.csvimport.flow.instructions.DefaultImportSteps
import com.exparo.importdata.csvimport.flow.instructions.FinancistoSteps
import com.exparo.importdata.csvimport.flow.instructions.FortuneCitySteps
import com.exparo.importdata.csvimport.flow.instructions.ExparoWalletSteps
import com.exparo.importdata.csvimport.flow.instructions.KTWMoneyManagerSteps
import com.exparo.importdata.csvimport.flow.instructions.MonefySteps
import com.exparo.importdata.csvimport.flow.instructions.MoneyManagerPraseSteps
import com.exparo.importdata.csvimport.flow.instructions.OneMoneySteps
import com.exparo.importdata.csvimport.flow.instructions.SpendeeSteps
import com.exparo.importdata.csvimport.flow.instructions.WalletByBudgetBakersSteps
import com.exparo.legacy.domain.deprecated.logic.csv.model.ImportType

@Composable
fun ImportType.ImportSteps(
    onUploadClick: () -> Unit
) {
    when (this) {
        ImportType.EXPARO -> {
            ExparoWalletSteps(
                onUploadClick = onUploadClick
            )
        }

        ImportType.MONEY_MANAGER -> {
            MoneyManagerPraseSteps(
                onUploadClick = onUploadClick
            )
        }

        ImportType.WALLET_BY_BUDGET_BAKERS -> {
            WalletByBudgetBakersSteps(
                onUploadClick = onUploadClick
            )
        }

        ImportType.SPENDEE -> SpendeeSteps(
            onUploadClick = onUploadClick
        )

        ImportType.MONEFY -> MonefySteps(
            onUploadClick = onUploadClick
        )

        ImportType.ONE_MONEY -> OneMoneySteps(
            onUploadClick = onUploadClick
        )

        ImportType.BLUE_COINS -> DefaultImportSteps(
            onUploadClick = onUploadClick
        )

        ImportType.KTW_MONEY_MANAGER -> KTWMoneyManagerSteps(
            onUploadClick = onUploadClick
        )

        ImportType.FORTUNE_CITY -> FortuneCitySteps(
            onUploadClick = onUploadClick
        )

        ImportType.FINANCISTO -> FinancistoSteps(
            onUploadClick = onUploadClick
        )
    }
}
