package com.exparo.onboarding

import androidx.compose.runtime.Immutable
import com.exparo.data.model.Category
import com.exparo.legacy.data.model.AccountBalance
import com.exparo.wallet.domain.data.ExparoCurrency
import com.exparo.wallet.domain.deprecated.logic.model.CreateAccountData
import com.exparo.wallet.domain.deprecated.logic.model.CreateCategoryData
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class OnboardingDetailState(
    val currency: ExparoCurrency,
    val accounts: ImmutableList<AccountBalance>,
    val accountSuggestions: ImmutableList<CreateAccountData>,
    val categories: ImmutableList<Category>,
    val categorySuggestions: ImmutableList<CreateCategoryData>
)
