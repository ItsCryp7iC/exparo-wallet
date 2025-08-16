package com.exparo.loans.loandetails.events

import com.exparo.legacy.datamodel.Loan

sealed interface LoanModalEvent : LoanDetailsScreenEvent {
    data object OnDismissLoanModal : LoanModalEvent
    data class OnEditLoanModal(val loan: Loan, val createLoanTransaction: Boolean) :
        LoanModalEvent

    data object PerformCalculation : LoanModalEvent
    data object OnChangeDate : LoanModalEvent
    data object OnChangeTime : LoanModalEvent
}
