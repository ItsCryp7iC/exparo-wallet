package com.exparo.data.model

import com.exparo.data.model.primitive.AssetCode
import com.exparo.data.model.primitive.ColorInt
import com.exparo.data.model.primitive.IconAsset
import com.exparo.data.model.primitive.NotBlankTrimmedString
import com.exparo.data.model.sync.Identifiable
import com.exparo.data.model.sync.UniqueId
import java.util.UUID

@JvmInline
value class AccountId(override val value: UUID) : UniqueId

data class Account(
    override val id: AccountId,
    val name: NotBlankTrimmedString,
    val asset: AssetCode,
    val color: ColorInt,
    val icon: IconAsset?,
    val includeInBalance: Boolean,
    override val orderNum: Double,
) : Identifiable<AccountId>, Reorderable
