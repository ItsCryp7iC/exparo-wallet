package com.exparo.wallet.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exparo.design.l0_system.UI
import com.exparo.legacy.ExparoWalletComponentPreview
import com.exparo.wallet.ui.theme.*

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    notFilledColor: Color = UI.colors.pure,
    positiveProgress: Boolean = true,
    percent: Double
) {
    Spacer(
        modifier = modifier
            .clip(UI.shapes.r4)
            .background(notFilledColor)
            .drawBehind {
                drawRect(
                    color = when {
                        percent <= 0.25 -> {
                            if (positiveProgress) Red else Green
                        }
                        percent <= 0.50 -> {
                            if (positiveProgress) Orange else Exparo
                        }
                        percent <= 0.75 -> {
                            if (positiveProgress) Exparo else Orange
                        }
                        else -> if (positiveProgress) Green else Red
                    },
                    size = size.copy(
                        width = (size.width * percent).toFloat()
                    )
                )
            },
    )
}

@Preview
@Composable
private fun Preview() {
    ExparoWalletComponentPreview {
        ProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 16.dp),
            notFilledColor = Gray,
            percent = 0.6
        )
    }
}
