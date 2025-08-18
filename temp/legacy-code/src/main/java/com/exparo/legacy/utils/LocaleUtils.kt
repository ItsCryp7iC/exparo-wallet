package com.exparo.legacy.utils

import java.util.Locale

fun toBengaliNumerals(input: String): String {
    if (!isBengaliLocale()) return input
    val englishNumerals = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val bengaliNumerals = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    val builder = StringBuilder()
    for (char in input) {
        val index = englishNumerals.indexOf(char)
        if (index != -1) {
            builder.append(bengaliNumerals[index])
        } else {
            builder.append(char)
        }
    }
    return builder.toString()
}

fun toEnglishNumerals(input: String): String {
    val englishNumerals = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val bengaliNumerals = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    val builder = StringBuilder()
    for (char in input) {
        val index = bengaliNumerals.indexOf(char)
        if (index != -1) {
            builder.append(englishNumerals[index])
        } else {
            builder.append(char)
        }
    }
    return builder.toString()
}
