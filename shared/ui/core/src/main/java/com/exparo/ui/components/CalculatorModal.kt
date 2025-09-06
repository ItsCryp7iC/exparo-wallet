package com.exparo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.exparo.design.system.ExparoMaterial3Theme
import com.exparo.ui.R

/**
 * A modern calculator modal dialog using Material3 design system.
 * Provides a quick calculator interface for on-the-fly calculations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialExpression: String = "",
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                CalculatorContent(
                    initialExpression = initialExpression,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun CalculatorContent(
    initialExpression: String,
    onDismiss: () -> Unit,
) {
    var expression by remember { mutableStateOf(initialExpression) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = stringResource(R.string.calculator),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Expression Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (expression.isBlank()) {
                        stringResource(R.string.calculator_empty_expression)
                    } else {
                        expression
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (expression.isBlank()) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calculator Keypad
        CalculatorKeypad(
            onNumberClick = { number ->
                expression += number
            },
            onOperatorClick = { operator ->
                expression = handleOperator(expression, operator)
            },
            onClearClick = {
                expression = ""
            },
            onBackspaceClick = {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                }
            },
            onEqualsClick = {
                val result = calculateExpression(expression)
                if (result != null) {
                    expression = result.toString()
                }
            },
            onDecimalClick = {
                expression = handleDecimal(expression)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Close")
            }
        }
    }
}

@Composable
private fun CalculatorKeypad(
    onNumberClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onEqualsClick: () -> Unit,
    onDecimalClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First Row: C, (, ), ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CalculatorButton(
                text = "C",
                onClick = onClearClick,
                backgroundColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                testTag = "calc_key_C"
            )
            CalculatorButton(
                text = "(",
                onClick = { onOperatorClick("(") },
                testTag = "calc_key_("
            )
            CalculatorButton(
                text = ")",
                onClick = { onOperatorClick(")") },
                testTag = "calc_key_)"
            )
            CalculatorButton(
                text = "÷",
                onClick = { onOperatorClick("÷") },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                testTag = "calc_key_÷"
            )
        }

        // Second Row: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CalculatorButton(text = "7", onClick = { onNumberClick("7") }, testTag = "calc_key_7")
            CalculatorButton(text = "8", onClick = { onNumberClick("8") }, testTag = "calc_key_8")
            CalculatorButton(text = "9", onClick = { onNumberClick("9") }, testTag = "calc_key_9")
            CalculatorButton(
                text = "×",
                onClick = { onOperatorClick("×") },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                testTag = "calc_key_×"
            )
        }

        // Third Row: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CalculatorButton(text = "4", onClick = { onNumberClick("4") }, testTag = "calc_key_4")
            CalculatorButton(text = "5", onClick = { onNumberClick("5") }, testTag = "calc_key_5")
            CalculatorButton(text = "6", onClick = { onNumberClick("6") }, testTag = "calc_key_6")
            CalculatorButton(
                text = "−",
                onClick = { onOperatorClick("−") },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                testTag = "calc_key_−"
            )
        }

        // Fourth Row: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CalculatorButton(text = "1", onClick = { onNumberClick("1") }, testTag = "calc_key_1")
            CalculatorButton(text = "2", onClick = { onNumberClick("2") }, testTag = "calc_key_2")
            CalculatorButton(text = "3", onClick = { onNumberClick("3") }, testTag = "calc_key_3")
            CalculatorButton(
                text = "+",
                onClick = { onOperatorClick("+") },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                testTag = "calc_key_+"
            )
        }

        // Fifth Row: 0, ., Backspace, =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            CalculatorButton(
                text = "0",
                onClick = { onNumberClick("0") },
                testTag = "calc_key_0"
            )
            CalculatorButton(
                text = ".",
                onClick = onDecimalClick,
                testTag = "calc_key_decimal"
            )
            CalculatorButton(
                text = "⌫",
                onClick = onBackspaceClick,
                backgroundColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                testTag = "calc_key_backspace"
            )
            CalculatorButton(
                text = "=",
                onClick = onEqualsClick,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                testTag = "calc_key_="
            )
        }
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    testTag: String = "",
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor,
            textAlign = TextAlign.Center
        )
    }
}

// Helper functions
private fun handleOperator(expression: String, operator: String): String {
    return if (expression.isNotEmpty() && expression.last().isOperator()) {
        expression.dropLast(1) + operator
    } else {
        expression + operator
    }
}

private fun handleDecimal(expression: String): String {
    // Check if current number already has a decimal point
    val lastNumberPart = expression.takeLastWhile { !it.isOperator() && it != '(' && it != ')' }
    return if (!lastNumberPart.contains('.')) {
        if (lastNumberPart.isEmpty() || lastNumberPart.all { it.isOperator() }) {
            expression + "0."
        } else {
            expression + "."
        }
    } else {
        expression
    }
}

private fun Char.isOperator(): Boolean = when (this) {
    '+', '−', '×', '÷' -> true
    else -> false
}

private fun calculateExpression(expression: String): Double? {
    return try {
        // Convert display symbols to calculation symbols
        val calculationExpression = buildString {
            for (char in expression) {
                when (char) {
                    '÷' -> append('/')
                    '×' -> append('*')
                    '−' -> append('-')
                    else -> append(char)
                }
            }
        }
        
        // Handle negative numbers by adding zero in front if expression starts with minus
        val normalizedExpression = if (calculationExpression.startsWith("-")) {
            "0$calculationExpression"
        } else {
            calculationExpression
        }
        
        // Simple evaluation using Kotlin's built-in functionality
        evaluateSimpleExpression(normalizedExpression)
    } catch (e: Exception) {
        null
    }
}

// Simple expression evaluator for basic arithmetic with proper operator precedence
private fun evaluateSimpleExpression(expression: String): Double? {
    return try {
        if (expression.isBlank()) return null
        
        // Remove spaces and handle parentheses
        val cleanExpression = expression.replace(" ", "")
        
        // Split into tokens (numbers and operators)
        val tokens = parseExpression(cleanExpression)
        if (tokens.isEmpty()) return null
        
        // Evaluate with proper operator precedence
        evaluateTokens(tokens)
    } catch (e: Exception) {
        null
    }
}

// Parse expression into tokens (numbers and operators)
private fun parseExpression(expression: String): List<String> {
    val tokens = mutableListOf<String>()
    var currentNumber = ""
    
    for (i in expression.indices) {
        val char = expression[i]
        when {
            char.isDigit() || char == '.' -> {
                currentNumber += char
            }
            char in listOf('+', '-', '*', '/') -> {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber)
                    currentNumber = ""
                }
                // Handle negative numbers
                if (char == '-' && (i == 0 || expression[i-1] in listOf('+', '-', '*', '/', '('))) {
                    currentNumber += char
                } else {
                    tokens.add(char.toString())
                }
            }
            char == '(' -> {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber)
                    currentNumber = ""
                }
                tokens.add(char.toString())
            }
            char == ')' -> {
                if (currentNumber.isNotEmpty()) {
                    tokens.add(currentNumber)
                    currentNumber = ""
                }
                tokens.add(char.toString())
            }
        }
    }
    
    if (currentNumber.isNotEmpty()) {
        tokens.add(currentNumber)
    }
    
    return tokens
}

// Evaluate tokens with proper operator precedence (* and / before + and -)
private fun evaluateTokens(tokens: List<String>): Double? {
    if (tokens.isEmpty()) return null
    
    // Handle parentheses first
    val tokensWithoutParens = evaluateParentheses(tokens.toMutableList())
    
    // Convert to postfix notation and evaluate
    return evaluateInfixExpression(tokensWithoutParens)
}

// Handle parentheses by recursively evaluating inner expressions
private fun evaluateParentheses(tokens: MutableList<String>): List<String> {
    while (tokens.contains("(")) {
        var openIndex = -1
        var closeIndex = -1
        
        // Find innermost parentheses
        for (i in tokens.indices) {
            if (tokens[i] == "(") {
                openIndex = i
            } else if (tokens[i] == ")" && openIndex != -1) {
                closeIndex = i
                break
            }
        }
        
        if (openIndex != -1 && closeIndex != -1) {
            val innerTokens = tokens.subList(openIndex + 1, closeIndex)
            val result = evaluateInfixExpression(innerTokens)
            
            if (result != null) {
                // Replace parentheses and content with result
                for (j in closeIndex downTo openIndex) {
                    tokens.removeAt(j)
                }
                tokens.add(openIndex, result.toString())
            } else {
                break
            }
        } else {
            break
        }
    }
    
    return tokens
}

// Evaluate infix expression with operator precedence
private fun evaluateInfixExpression(tokens: List<String>): Double? {
    if (tokens.isEmpty()) return null
    if (tokens.size == 1) return tokens[0].toDoubleOrNull()
    
    val numbers = mutableListOf<Double>()
    val operators = mutableListOf<String>()
    
    // Parse tokens into numbers and operators
    for (i in tokens.indices) {
        val token = tokens[i]
        
        if (token.toDoubleOrNull() != null) {
            numbers.add(token.toDouble())
        } else if (token in listOf("+", "-", "*", "/")) {
            // Apply higher precedence operators immediately
            while (operators.isNotEmpty() && 
                   hasHigherOrEqualPrecedence(operators.last(), token) &&
                   numbers.size >= 2) {
                
                val b = numbers.removeAt(numbers.size - 1)
                val a = numbers.removeAt(numbers.size - 1)
                val op = operators.removeAt(operators.size - 1)
                
                val result = applyOperator(a, b, op)
                if (result != null) {
                    numbers.add(result)
                } else {
                    return null
                }
            }
            operators.add(token)
        }
    }
    
    // Apply remaining operators
    while (operators.isNotEmpty() && numbers.size >= 2) {
        val b = numbers.removeAt(numbers.size - 1)
        val a = numbers.removeAt(numbers.size - 1)
        val op = operators.removeAt(operators.size - 1)
        
        val result = applyOperator(a, b, op)
        if (result != null) {
            numbers.add(result)
        } else {
            return null
        }
    }
    
    return if (numbers.size == 1) numbers[0] else null
}

// Check operator precedence
private fun hasHigherOrEqualPrecedence(op1: String, op2: String): Boolean {
    val precedence = mapOf(
        "+" to 1, "-" to 1,
        "*" to 2, "/" to 2
    )
    
    return (precedence[op1] ?: 0) >= (precedence[op2] ?: 0)
}

// Apply arithmetic operation
private fun applyOperator(a: Double, b: Double, operator: String): Double? {
    return when (operator) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b != 0.0) a / b else null
        else -> null
    }
}

@Preview
@Composable
private fun CalculatorModalPreview() {
    ExparoMaterial3Theme(dark = false, isTrueBlack = false) {
        CalculatorModal(
            visible = true,
            onDismiss = { },
            initialExpression = "2+3"
        )
    }
}

@Preview
@Composable
private fun CalculatorModalDarkPreview() {
    ExparoMaterial3Theme(dark = true, isTrueBlack = false) {
        CalculatorModal(
            visible = true,
            onDismiss = { },
            initialExpression = ""
        )
    }
}