package com.exparo.legacy.datamodel.temp

import com.exparo.data.db.entity.ExchangeRateEntity
import com.exparo.legacy.datamodel.ExchangeRate

fun ExchangeRateEntity.toLegacyDomain(): ExchangeRate = ExchangeRate(
    baseCurrency = baseCurrency,
    currency = currency,
    rate = rate
)
