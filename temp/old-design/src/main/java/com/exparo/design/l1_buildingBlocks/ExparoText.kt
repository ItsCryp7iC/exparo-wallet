package com.exparo.design.l1_buildingBlocks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.exparo.design.l1_buildingBlocks.data.ExparoPadding
import com.exparo.design.utils.exparoPadding
import com.exparo.design.utils.thenIf

@Deprecated("Old design system. Use `:exparo-design` and Material3")
@Composable
fun ExparoText(
    modifier: Modifier = Modifier,
    text: String,
    typo: TextStyle,
    padding: ExparoPadding? = null
) {
    Text(
        modifier = Modifier
            .thenIf(padding != null) {
                exparoPadding(exparoPadding = padding!!)
            }
            .then(modifier),
        text = text,
        style = typo,
    )
}
