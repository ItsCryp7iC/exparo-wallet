package com.exparo.data.model

import com.exparo.data.model.primitive.AssetCode
import com.exparo.data.model.primitive.PositiveDouble

data class ExchangeRate(
    val baseCurrency: AssetCode,
    val currency: AssetCode,
    val rate: PositiveDouble,
    val manualOverride: Boolean,
)
