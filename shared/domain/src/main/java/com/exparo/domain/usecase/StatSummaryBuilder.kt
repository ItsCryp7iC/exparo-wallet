package com.exparo.domain.usecase

import com.exparo.data.model.PositiveValue
import com.exparo.data.model.primitive.AssetCode
import com.exparo.data.model.primitive.NonNegativeInt
import com.exparo.data.model.primitive.PositiveDouble
import com.exparo.domain.model.StatSummary

class StatSummaryBuilder {
    private var count = 0
    private val values = mutableMapOf<AssetCode, PositiveDouble>()

    fun process(value: PositiveValue) {
        count++
        val asset = value.asset
        // Because it might overflow
        PositiveDouble.from(
            (values[asset]?.value ?: 0.0) + value.amount.value
        ).onRight { newValue ->
            values[asset] = newValue
        }
    }

    fun build(): StatSummary = StatSummary(
        trnCount = NonNegativeInt.unsafe(count),
        values = values,
    )
}