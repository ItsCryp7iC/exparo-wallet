package com.exparo.legacy.ui.component.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.exparo.design.l0_system.UI
import com.exparo.design.l0_system.style
import com.exparo.legacy.ExparoWalletComponentPreview
import com.exparo.legacy.utils.dateNowLocal
import com.exparo.legacy.utils.dateNowUTC
import com.exparo.legacy.utils.format
import com.exparo.legacy.utils.formatLocal
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.Gray
import com.exparo.wallet.ui.theme.Green
import java.time.LocalDate

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun HistoryDateDivider(
    date: LocalDate,
    spacerTop: Dp,
    baseCurrency: String,
    income: Double,
    expenses: Double
) {
    Spacer(Modifier.height(spacerTop))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(24.dp))

        val today = dateNowLocal()

        Column {
            Text(
                text = date.formatLocal(
                    if (today.year == date.year) "MMMM dd." else "MMM dd. yyy"
                ),
                style = UI.typo.b1.style(
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = when (date) {
                    today -> {
                        stringResource(R.string.today)
                    }
                    today.minusDays(1) -> {
                        stringResource(R.string.yesterday)
                    }
                    today.plusDays(1) -> {
                        stringResource(R.string.tomorrow)
                    }
                    else -> {
                        date.formatLocal("EEEE")
                    }
                },
                style = UI.typo.c.style(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(Modifier.weight(1f))

        val cashflow = income - expenses
        Text(
            text = "${cashflow.format(baseCurrency)} $baseCurrency",
            style = UI.typo.nB2.style(
                fontWeight = FontWeight.Bold,
                color = if (cashflow > 0) Green else Gray
            )
        )

        Spacer(Modifier.width(32.dp))
    }

    Spacer(Modifier.height(4.dp))
}

@Preview
@Composable
private fun Preview_Today() {
    ExparoWalletComponentPreview {
        HistoryDateDivider(
            date = dateNowUTC(),
            spacerTop = 32.dp,
            baseCurrency = "BGN",
            income = 13.50,
            expenses = 256.13
        )
    }
}

@Preview
@Composable
private fun Preview_Yesterday() {
    ExparoWalletComponentPreview {
        HistoryDateDivider(
            date = dateNowUTC().minusDays(1),
            spacerTop = 32.dp,
            baseCurrency = "BGN",
            income = 13.50,
            expenses = 256.13
        )
    }
}

@Preview
@Composable
private fun Preview_OneYear_Ago() {
    ExparoWalletComponentPreview {
        HistoryDateDivider(
            date = dateNowUTC().minusYears(1),
            spacerTop = 32.dp,
            baseCurrency = "BGN",
            income = 13.50,
            expenses = 256.13
        )
    }
}
