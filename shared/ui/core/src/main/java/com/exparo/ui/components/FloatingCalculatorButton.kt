package com.exparo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exparo.design.system.ExparoMaterial3Theme

/**
 * A floating action button specifically for the calculator feature.
 * Uses Material3 design system with custom styling for Exparo Wallet.
 */
@Composable
fun FloatingCalculatorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.testTag("floating_calculator_button"),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Calculate,
            contentDescription = "Calculator",
        )
    }
}

@Preview
@Composable
private fun FloatingCalculatorButtonPreview() {
    ExparoMaterial3Theme(dark = false, isTrueBlack = false) {
        FloatingCalculatorButton(
            onClick = { }
        )
    }
}

@Preview
@Composable
private fun FloatingCalculatorButtonDarkPreview() {
    ExparoMaterial3Theme(dark = true, isTrueBlack = false) {
        FloatingCalculatorButton(
            onClick = { }
        )
    }
}