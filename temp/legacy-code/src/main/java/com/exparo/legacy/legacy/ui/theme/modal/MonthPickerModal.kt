package com.exparo.wallet.ui.theme.modal

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exparo.design.l0_system.UI
import com.exparo.design.l0_system.style
import com.exparo.design.utils.thenIf
import com.exparo.legacy.ExparoWalletPreview
import com.exparo.legacy.data.model.Month
import com.exparo.legacy.data.model.Month.Companion.monthsList
import com.exparo.legacy.utils.dateNowUTC
import com.exparo.legacy.utils.drawColoredShadow
import com.exparo.legacy.utils.hideKeyboard
import com.exparo.legacy.utils.onScreenStart
import com.exparo.ui.R
import com.exparo.wallet.ui.theme.Gradient
import com.exparo.wallet.ui.theme.Exparo
import com.exparo.wallet.ui.theme.components.WrapContentRow
import com.exparo.wallet.ui.theme.findContrastTextColor
import java.time.LocalDate
import java.util.UUID

@SuppressLint("ComposeModifierMissing")
@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Suppress("ParameterNaming")
@Composable
fun BoxWithConstraintsScope.MonthPickerModal(
    initialDate: LocalDate,
    visible: Boolean,
    dismiss: () -> Unit,
    onMonthSelected: (Int) -> Unit,
    id: UUID = UUID.randomUUID(),
) {
    var selectedMonth by remember {
        mutableStateOf(initialDate.monthValue)
    }

    ExparoModal(
        id = id,
        visible = visible,
        dismiss = dismiss,
        PrimaryAction = {
            ModalSave {
                onMonthSelected(selectedMonth)
                dismiss()
            }
        }
    ) {
        val view = LocalView.current
        onScreenStart {
            hideKeyboard(view)
        }

        Spacer(Modifier.height(32.dp))

        ModalTitle(
            text = stringResource(R.string.choose_month)
        )

        Spacer(Modifier.height(24.dp))

        MonthPicker(
            selectedMonth = selectedMonth,
            onMonthSelected = {
                selectedMonth = it
            }
        )

        Spacer(Modifier.height(56.dp))
    }
}

@Composable
@Suppress("ParameterNaming")
private fun MonthPicker(
    selectedMonth: Int,
    onMonthSelected: (Int) -> Unit
) {
    val months = monthsList()

    WrapContentRow(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        horizontalMarginBetweenItems = 12.dp,
        verticalMarginBetweenRows = 12.dp,
        items = months
    ) {
        MonthButton(
            month = it,
            selected = it.monthValue == selectedMonth
        ) {
            onMonthSelected(it.monthValue)
        }
    }
}

@Composable
private fun MonthButton(
    month: Month,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val monthColor = Exparo

    val medium = UI.colors.medium
    val rFull = UI.shapes.rFull

    Text(
        modifier = Modifier
            .thenIf(selected) {
                drawColoredShadow(monthColor)
            }
            .clip(UI.shapes.rFull)
            .clickable(onClick = onClick)
            .thenIf(!selected) {
                border(2.dp, medium, rFull)
            }
            .thenIf(selected) {
                background(
                    brush = Gradient
                        .solid(monthColor)
                        .asHorizontalBrush(),
                    rFull
                )
            }
            .padding(horizontal = 40.dp, vertical = 12.dp),
        text = month.name,
        style = UI.typo.b2.style(
            color = if (selected) {
                findContrastTextColor(monthColor)
            } else {
                UI.colors.pureInverse
            },
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Preview
@Composable
private fun Preview() {
    ExparoWalletPreview {
        MonthPickerModal(
            initialDate = dateNowUTC(),
            visible = true,
            dismiss = {},
            onMonthSelected = {}
        )
    }
}
