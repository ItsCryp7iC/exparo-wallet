package com.exparo.legacy.datamodel.temp

import com.exparo.data.db.entity.SettingsEntity
import com.exparo.legacy.datamodel.Settings

fun SettingsEntity.toLegacyDomain(): Settings = Settings(
    theme = theme,
    baseCurrency = currency,
    bufferAmount = bufferAmount.toBigDecimal(),
    name = name,
    id = id
)
