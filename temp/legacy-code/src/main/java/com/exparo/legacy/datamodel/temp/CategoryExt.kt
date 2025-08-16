package com.exparo.legacy.datamodel.temp

import com.exparo.data.db.entity.CategoryEntity
import com.exparo.legacy.datamodel.Category

fun CategoryEntity.toLegacyDomain(): Category = Category(
    name = name,
    color = color,
    icon = icon,
    orderNum = orderNum,
    isSynced = isSynced,
    isDeleted = isDeleted,
    id = id
)
