package com.exparo.home.customerjourney

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.exparo.base.time.TimeProvider
import com.exparo.design.l0_system.Gradient
import com.exparo.domain.RootScreen
import com.exparo.legacy.ExparoWalletCtx
import com.exparo.navigation.Navigation
import com.exparo.poll.data.PollRepository

@Immutable
data class CustomerJourneyCardModel(
    val id: String,
    @Suppress("MaximumLineLength", "ParameterWrapping", "MaxLineLength", "ParameterListWrapping")
    val condition: suspend (trnCount: Long, plannedPaymentsCount: Long, exparoContext: ExparoWalletCtx, deps: CustomerJourneyDeps) -> Boolean,
    val title: String,
    val description: String,
    val cta: String?,
    @DrawableRes val ctaIcon: Int,

    val hasDismiss: Boolean = true,

    val background: Gradient,
    val onAction: (Navigation, ExparoWalletCtx, RootScreen) -> Unit
)

@Immutable
data class CustomerJourneyDeps(
    val pollRepository: PollRepository,
    val timeProvider: TimeProvider,
)
