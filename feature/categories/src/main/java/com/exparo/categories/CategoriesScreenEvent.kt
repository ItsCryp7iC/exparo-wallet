package com.exparo.categories

import com.exparo.wallet.domain.data.SortOrder
import com.exparo.wallet.domain.deprecated.logic.model.CreateCategoryData
import com.exparo.wallet.ui.theme.modal.edit.CategoryModalData

sealed interface CategoriesScreenEvent {
    data class OnReorder(
        val newOrder: List<CategoryData>,
        val sortOrder: SortOrder = SortOrder.DEFAULT
    ) : CategoriesScreenEvent

    data class OnCreateCategory(val createCategoryData: CreateCategoryData) :
        CategoriesScreenEvent

    data class OnReorderModalVisible(val visible: Boolean) : CategoriesScreenEvent
    data class OnSortOrderModalVisible(val visible: Boolean) : CategoriesScreenEvent
    data class OnCategoryModalVisible(val categoryModalData: CategoryModalData?) :
        CategoriesScreenEvent
    data class OnSearchQueryUpdate(val queryString: String) : CategoriesScreenEvent
}
