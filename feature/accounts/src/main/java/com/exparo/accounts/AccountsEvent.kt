package com.exparo.accounts

sealed interface AccountsEvent {
    data class OnReorder(val reorderedList: List<com.exparo.legacy.data.model.AccountData>) :
        AccountsEvent
    data class OnReorderModalVisible(val reorderVisible: Boolean) : AccountsEvent
}
