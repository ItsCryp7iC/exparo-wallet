package com.exparo.legacy.utils

import com.exparo.wallet.domain.data.ExparoCurrency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.truncate

const val MILLION = 1000000
const val N_100K = 100000
const val THOUSAND = 1000

// Bengali numeral mapping
private val bengaliNumerals = mapOf(
    '0' to '০',
    '1' to '১',
    '2' to '২',
    '3' to '৩',
    '4' to '৪',
    '5' to '৫',
    '6' to '৬',
    '7' to '৭',
    '8' to '৮',
    '9' to '৯'
)

private val englishNumerals = mapOf(
    '০' to '0',
    '১' to '1',
    '২' to '2',
    '৩' to '3',
    '৪' to '4',
    '৫' to '5',
    '৬' to '6',
    '৭' to '7',
    '৮' to '8',
    '৯' to '9'
)

/**
 * Convert English numerals to Bengali numerals for display
 */
fun String.toBengaliNumerals(): String {
    return this.map { char ->
        bengaliNumerals[char] ?: char
    }.joinToString("")
}

/**
 * Convert Bengali numerals to English numerals for processing
 */
fun String.toEnglishNumerals(): String {
    return this.map { char ->
        englishNumerals[char] ?: char
    }.joinToString("")
}

/**
 * Check if the current locale is Bengali
 */
fun isBengaliLocale(): Boolean {
    val locale = java.util.Locale.getDefault()
    return locale.language == "bn" || locale.toString() == "bn_BD"
}

fun String.amountToDoubleOrNull(): Double? {
    return this.normalizeAmount().toDoubleOrNull()
}

fun String.amountToDouble(): Double {
    return this.normalizeAmount().toDouble()
}

fun String.normalizeAmount(): String {
    return this.removeGroupingSeparator()
        .normalizeDecimalSeparator()
}

fun String.normalizeExpression(): String {
    return this.removeGroupingSeparator()
        .normalizeDecimalSeparator()
}

fun String.removeGroupingSeparator(): String {
    // Always use English numerals for processing
    val separator = DecimalFormatSymbols.getInstance().groupingSeparator.toString()
    return this.toEnglishNumerals().replace(separator, "")
}

fun String.normalizeDecimalSeparator(): String {
    // Always use English numerals for processing
    val separator = DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    return this.toEnglishNumerals().replace(separator, ".")
}

fun localDecimalSeparator(): String {
    val separator = DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    // For Bengali locale, display Bengali numeral but return English for processing
    return if (isBengaliLocale()) separator.toBengaliNumerals() else separator
}

fun localGroupingSeparator(): String {
    val separator = DecimalFormatSymbols.getInstance().groupingSeparator.toString()
    // For Bengali locale, display Bengali numeral but return English for processing
    return if (isBengaliLocale()) separator.toBengaliNumerals() else separator
}

// Display Formatting
fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun Double.format(currencyCode: String): String {
    return this.format(ExparoCurrency.fromCode(currencyCode))
}

fun Double.format(currency: ExparoCurrency?): String {
    return if (currency?.isCrypto == true) {
        val result = this.formatCrypto()
        return when {
            result.lastOrNull() == localDecimalSeparator().firstOrNull() -> {
                val newResult = result.dropLast(1)
                if (newResult.isEmpty()) "0" else newResult
            }

            result.isEmpty() -> {
                "0"
            }

            else -> result
        }
    } else {
        formatFIAT()
    }
}

fun Double.formatCrypto(): String {
    val pattern = "###,###,##0.${"0".repeat(9)}"
    val format = DecimalFormat(pattern)
    val numberStringWithZeros = format.format(this)

    var lastTrailingZeroIndex: Int? = null
    for (i in numberStringWithZeros.lastIndex.downTo(0)) {
        if (numberStringWithZeros[i] == '0') {
            lastTrailingZeroIndex = i
        } else {
            break
        }
    }

    return if (lastTrailingZeroIndex != null) {
        numberStringWithZeros.substring(0, lastTrailingZeroIndex)
    } else {
        numberStringWithZeros
    }
}

private fun Double.formatFIAT(): String {
    val formatted = DecimalFormat("#,##0.00").format(this)
    // For Bengali locale, display Bengali numerals
    return if (isBengaliLocale()) formatted.toBengaliNumerals() else formatted
}

fun shortenAmount(amount: Double): String {
    return when {
        abs(amount) >= MILLION -> {
            formatShortenedNumber(amount / MILLION, "m")
        }

        abs(amount) >= THOUSAND -> {
            formatShortenedNumber(amount / THOUSAND, "k")
        }

        else -> amount.toString()
    }
}

private fun formatShortenedNumber(
    number: Double,
    extension: String,
): String {
    return if (hasSignificantDecimalPart(number)) {
        "${number.format(2)}$extension"
    } else {
        "${number.toInt()}$extension"
    }
}

fun hasSignificantDecimalPart(number: Double): Boolean {
    // TODO: Review, might cause trouble when integrating crypto
    val intPart = number.toInt()
    return abs(number - intPart) >= 0.009
}

fun shouldShortAmount(amount: Double): Boolean {
    return abs(amount) >= N_100K
}

fun formatInt(number: Int): String {
    val formatted = DecimalFormat("#,###,###,###").format(number)
    // For Bengali locale, display Bengali numerals
    return if (isBengaliLocale()) formatted.toBengaliNumerals() else formatted
}

fun decimalPartFormatted(currency: String, value: Double): String {
    return if (ExparoCurrency.fromCode(currency)?.isCrypto == true) {
        val decimalPartFormatted = value.formatCrypto()
            .split(localDecimalSeparator())
            .getOrNull(1) ?: "null"
        if (decimalPartFormatted.isNotBlank()) {
            "${localDecimalSeparator()}$decimalPartFormatted"
        } else {
            ""
        }
    } else {
        "${localDecimalSeparator()}${decimalPartFormattedFIAT(value)}"
    }
}

private fun decimalPartFormattedFIAT(value: Double): String {
    return DecimalFormat(".00").format(value)
        .split(localDecimalSeparator())
        .getOrNull(1)
        ?: value.toString()
            .split(localDecimalSeparator())
            .getOrNull(1)
        ?: "null"
}

fun Long.length() = when (this) {
    0L -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

fun formatInputAmount(
    currency: String,
    amount: String,
    newSymbol: String,
    decimalCountMax: Int = 2,
): String? {
    // Always use English numerals for processing
    val processedAmount = amount.toEnglishNumerals()
    val processedNewSymbol = newSymbol.toEnglishNumerals()
    
    val newlyEnteredNumberString = processedAmount + processedNewSymbol

    // Use English decimal separator for processing
    val separator = DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    val decimalPartString = newlyEnteredNumberString
        .split(separator)
        .getOrNull(1)
    val decimalCount = decimalPartString?.length ?: 0

    val amountDouble = newlyEnteredNumberString.amountToDoubleOrNull()

    val decimalCountOkay = ExparoCurrency.fromCode(currency)?.isCrypto == true ||
            decimalCount <= decimalCountMax
    if (amountDouble != null && decimalCountOkay) {
        val intPart = truncate(amountDouble).toInt()
        val decimalPartFormatted = if (decimalPartString != null) {
            // Display Bengali numerals for the decimal separator if in Bengali locale
            val displaySeparator = if (isBengaliLocale()) separator.toBengaliNumerals() else separator
            "$displaySeparator$decimalPartString"
        } else {
            ""
        }

        // Format the integer part (will display Bengali numerals if in Bengali locale)
        return formatInt(intPart) + decimalPartFormatted
    }

    return null
}

/**
toInt on numbers in the range (-1.0, 0.0) (exclusive of boundaries) will produce a positive int 0
So, this function append negative sign in that case
 */
fun integerPartFormatted(value: Double): String {
    val preciseValue = value.toBigDecimal()
    val formattedValue = DecimalFormat("###,###").format(preciseValue.toInt())
    return if (value > -1.0 && value < 0.0) {
        "-$formattedValue"
    } else {
        formattedValue
    }
}
