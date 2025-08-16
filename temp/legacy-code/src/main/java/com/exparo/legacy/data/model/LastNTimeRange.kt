package com.exparo.legacy.data.model

import androidx.compose.runtime.Immutable
import com.exparo.base.time.TimeProvider
import com.exparo.data.model.IntervalType
import com.exparo.legacy.forDisplay
import com.exparo.legacy.incrementDate
import java.time.Instant

@Suppress("DataClassFunctions")
@Immutable
data class LastNTimeRange(
    val periodN: Int,
    val periodType: IntervalType,
) {
    fun fromDate(
        timeProvider: TimeProvider
    ): Instant = periodType.incrementDate(
        date = timeProvider.utcNow(),
        intervalN = -periodN.toLong()
    )

    fun forDisplay(): String =
        "$periodN ${periodType.forDisplay(periodN)}"
}
