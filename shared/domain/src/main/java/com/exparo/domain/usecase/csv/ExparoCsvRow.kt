package com.exparo.domain.usecase.csv

import com.exparo.base.model.TransactionType
import com.exparo.data.model.AccountId
import com.exparo.data.model.CategoryId
import com.exparo.data.model.TransactionId
import com.exparo.data.model.primitive.AssetCode
import com.exparo.data.model.primitive.NonNegativeDouble
import com.exparo.data.model.primitive.NotBlankTrimmedString
import com.exparo.data.model.primitive.PositiveDouble
import java.time.Instant

// TODO: Fix Exparo Explicit detekt false-positives
@SuppressWarnings("DataClassTypedIDs")
data class ExparoCsvRow(
    val date: Instant?,
    val title: NotBlankTrimmedString?,
    val category: CategoryId?,
    val account: AccountId,
    val amount: NonNegativeDouble,
    val currency: AssetCode,
    val type: TransactionType,
    val transferAmount: PositiveDouble?,
    val transferCurrency: AssetCode?,
    val toAccountId: AccountId?,
    val receiveAmount: PositiveDouble?,
    val receiveCurrency: AssetCode?,
    val description: NotBlankTrimmedString?,
    val dueData: Instant?,
    val id: TransactionId
) {
    companion object {
        val Columns = listOf(
            "Date",
            "Title",
            "Category",
            "Account",
            "Amount",
            "Currency",
            "Type",
            "Transfer Amount",
            "Transfer Currency",
            "To Account",
            "Receive Amount",
            "Receive Currency",
            "Description",
            "Due Date",
            "ID",
        )
    }
}