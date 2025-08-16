package com.exparo.loans.loan.data

import com.exparo.legacy.datamodel.Account
import com.exparo.legacy.datamodel.LoanRecord

data class DisplayLoanRecord(
    val loanRecord: LoanRecord,
    val account: Account? = null,
    val loanRecordCurrencyCode: String = "",
    val loanCurrencyCode: String = "",
    val loanRecordTransaction: Boolean = false,
)
