package com.exparo.domain.legacy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.exparo.design.EXPARO_COLOR_PICKER_COLORS_FREE
import com.exparo.design.EXPARO_COLOR_PICKER_COLORS_PREMIUM
import com.exparo.design.l0_system.UI
import com.exparo.design.l0_system.dynamicContrast
import com.exparo.design.l0_system.style
import com.exparo.design.l1_buildingBlocks.ExparoIcon
import com.exparo.design.utils.densityScope
import com.exparo.design.utils.thenIf
import com.exparo.frp.test.TestingContext
import com.exparo.legacy.frp.onScreenStart
import com.exparo.legacy.exparoWalletCtx
import com.exparo.navigation.navigation
import com.exparo.ui.R
import kotlinx.coroutines.launch

@Deprecated("Old design system. Use `:exparo-design` and Material3")
private data class ExparoColor(
    val color: Color,
    val premium: Boolean
)

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Suppress("ParameterNaming")
@Composable
fun ColumnScope.ExparoColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 32.dp),
        text = stringResource(R.string.choose_color),
        style = UI.typo.b2.style(
            color = UI.colors.pureInverse,
            fontWeight = FontWeight.ExtraBold
        )
    )

    Spacer(Modifier.height(16.dp))

    val freeExparoColors = EXPARO_COLOR_PICKER_COLORS_FREE
        .map {
            ExparoColor(
                color = it,
                premium = false
            )
        }

    val premiumExparoColors = EXPARO_COLOR_PICKER_COLORS_PREMIUM
        .map {
            ExparoColor(
                color = it,
                premium = true
            )
        }

    val exparoColors = freeExparoColors + premiumExparoColors

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    densityScope {
        onScreenStart {
            if (TestingContext.inTest) return@onScreenStart // listState.scrollToItem breaks the tests
            // java.lang.IllegalStateException: pending composition has not been applied

            val selectedColorIndex = exparoColors.indexOfFirst { it.color == selectedColor }
            if (selectedColorIndex != -1) {
                coroutineScope.launch {
                    listState.scrollToItem(
                        index = selectedColorIndex,
                        scrollOffset = 0
                    )
                }
            }
        }
    }

    val exparoContext = exparoWalletCtx()
    val navigation = navigation()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        state = listState
    ) {
        items(
            count = exparoColors.size
        ) { index ->
            ColorItem(
                index = index,
                exparoColor = exparoColors[index],
                selectedColor = selectedColor,
                onSelected = {
                    onColorSelected(it.color)
                }
            )
        }
    }
}

@Composable
@Suppress("ParameterNaming")
private fun ColorItem(
    index: Int,
    exparoColor: ExparoColor,
    selectedColor: Color,
    onSelected: (ExparoColor) -> Unit
) {
    val color = exparoColor.color
    val selected = color == selectedColor

    if (index == 0) {
        Spacer(Modifier.width(24.dp))
    }

    val exparoContext = exparoWalletCtx()
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(48.dp)
            .background(color, CircleShape)
            .thenIf(selected) {
                border(width = 4.dp, color = color.dynamicContrast(), CircleShape)
            }
            .clickable(onClick = {
                onSelected(exparoColor)
            })
            .testTag("color_item_${exparoColor.color.value}"),
        contentAlignment = Alignment.Center
    ) {
        if (exparoColor.premium && !exparoContext.isPremium) {
            ExparoIcon(
                icon = R.drawable.ic_custom_safe_s,
                tint = color.dynamicContrast()
            )
        }
    }

    Spacer(Modifier.width(if (selected) 16.dp else 24.dp))
}
