package com.exparo.domain.model

import com.exparo.data.model.primitive.AssetCode
import com.exparo.data.model.primitive.NonNegativeInt
import com.exparo.data.model.primitive.PositiveDouble

data class StatSummary(
    val trnCount: NonNegativeInt,
    val values: Map<AssetCode, PositiveDouble>,
) {
    companion object {
        val Zero = StatSummary(
            values = emptyMap(),
            trnCount = NonNegativeInt.Zero
        )
    }
}