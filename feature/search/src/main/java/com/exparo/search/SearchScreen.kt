package com.exparo.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exparo.base.legacy.Theme
import com.exparo.design.utils.ExparoComponentPreview
import com.exparo.legacy.data.AppBaseData
import com.exparo.legacy.ui.SearchInput
import com.exparo.legacy.ui.component.transaction.transactions
import com.exparo.legacy.utils.densityScope
import com.exparo.legacy.utils.keyboardOnlyWindowInsets
import com.exparo.legacy.utils.keyboardVisibleState
import com.exparo.legacy.utils.selectEndTextFieldValue
import com.exparo.navigation.ExparoPreview
import com.exparo.navigation.SearchScreen
import com.exparo.navigation.screenScopedViewModel
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.modal.DURATION_MODAL_ANIM
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SearchScreen(screen: SearchScreen) {
    val viewModel: SearchViewModel = screenScopedViewModel()
    val uiState = viewModel.uiState()

    SearchUi(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SearchUi(
    uiState: SearchState,
    onEvent: (SearchEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Spacer(Modifier.height(24.dp))

        val listState = rememberLazyListState()

        var searchQueryTextFieldValue by remember {
            mutableStateOf(selectEndTextFieldValue(uiState.searchQuery))
        }

        SearchInput(
            searchQueryTextFieldValue = searchQueryTextFieldValue,
            hint = stringResource(R.string.search_transactions),
            showClearIcon = searchQueryTextFieldValue.text.isNotEmpty(),
            onSetSearchQueryTextField = {
                searchQueryTextFieldValue = it
                onEvent(SearchEvent.Search(it.text))
            }
        )

        LaunchedEffect(uiState.transactions) {
            // scroll to top when transactions are changed
            listState.animateScrollToItem(index = 0, scrollOffset = 0)
        }

        Spacer(Modifier.height(16.dp))
        val emptyStateTitle = stringResource(R.string.no_transactions)
        val emptyStateText = stringResource(
            R.string.no_transactions_for_query,
            searchQueryTextFieldValue.text
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState

        ) {
            transactions(
                baseData = AppBaseData(
                    baseCurrency = uiState.baseCurrency,
                    accounts = uiState.accounts,
                    categories = uiState.categories
                ),
                upcoming = null,
                setUpcomingExpanded = { },
                overdue = null,
                setOverdueExpanded = { },
                history = uiState.transactions,
                onPayOrGet = { },
                emptyStateTitle = emptyStateTitle,
                emptyStateText = emptyStateText,
                dateDividerMarginTop = 16.dp,
                shouldShowAccountSpecificColorInTransactions = uiState.shouldShowAccountSpecificColorInTransactions
            )

            item {
                val keyboardVisible by keyboardVisibleState()
                val keyboardShownInsetDp by animateDpAsState(
                    targetValue = densityScope {
                        if (keyboardVisible) keyboardOnlyWindowInsets().bottom.toDp() else 0.dp
                    },
                    animationSpec = tween(DURATION_MODAL_ANIM)
                )

                Spacer(Modifier.height(keyboardShownInsetDp))
                // add keyboard height margin at bototm so the list can scroll to bottom
            }
        }
    }
}

@Preview
@Composable
private fun Preview(isDark: Boolean = false) {
    ExparoPreview(isDark) {
        SearchUi(
            uiState = SearchState(
                searchQuery = "",
                transactions = persistentListOf(),
                baseCurrency = "",
                accounts = persistentListOf(),
                categories = persistentListOf(),
                shouldShowAccountSpecificColorInTransactions = false
            ),
            onEvent = {}
        )
    }
}

/** For screenshot testing */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchUiTest(isDark: Boolean) {
    val theme = if (isDark) Theme.DARK else Theme.LIGHT
    ExparoComponentPreview(theme = theme) {
        Preview(isDark)
    }
}