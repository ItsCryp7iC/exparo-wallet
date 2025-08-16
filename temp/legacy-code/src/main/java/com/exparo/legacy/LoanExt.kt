package com.exparo.legacy

import com.exparo.base.legacy.stringRes
import com.exparo.data.model.LoanType
import com.exparo.legacy.datamodel.Loan
import com.exparo.ui.R

fun Loan.humanReadableType(): String {
    return if (type == LoanType.BORROW) {
        stringRes(R.string.borrowed_uppercase)
    } else {
        stringRes(R.string.lent_uppercase)
    }
}
