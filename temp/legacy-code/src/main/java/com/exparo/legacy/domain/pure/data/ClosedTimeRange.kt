package com.exparo.wallet.domain.pure.data

import com.exparo.base.time.TimeProvider
import com.exparo.legacy.utils.exparoMinTime
import java.time.Instant

data class ClosedTimeRange(
    val from: Instant,
    val to: Instant,
) {
    companion object {
        fun allTimeExparo(
            timeProvider: TimeProvider,
        ): ClosedTimeRange = ClosedTimeRange(
            from = exparoMinTime(),
            to = timeProvider.utcNow(),
        )

        fun to(to: Instant): ClosedTimeRange = ClosedTimeRange(
            from = exparoMinTime(),
            to = to
        )
    }
}
