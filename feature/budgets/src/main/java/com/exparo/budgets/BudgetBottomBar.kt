package com.exparo.budgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.exparo.legacy.ExparoWalletPreview
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.Blue
import com.exparo.wallet.ui.theme.components.BackBottomBar
import com.exparo.wallet.ui.theme.components.ExparoButton

@Composable
internal fun BoxWithConstraintsScope.BudgetBottomBar(
    onClose: () -> Unit,
    onAdd: () -> Unit
) {
    BackBottomBar(onBack = onClose) {
        ExparoButton(
            text = stringResource(R.string.add_budget),
            iconStart = R.drawable.ic_plus
        ) {
            onAdd()
        }
    }
}

@Preview
@Composable
private fun PreviewBottomBar() {
    com.exparo.legacy.ExparoWalletPreview {
        Column(
            Modifier
                .fillMaxSize()
                .background(Blue)
        ) {
        }

        BudgetBottomBar(
            onAdd = {},
            onClose = {}
        )
    }
}
