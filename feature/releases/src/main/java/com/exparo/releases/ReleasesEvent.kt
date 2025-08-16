package com.exparo.releases

sealed interface ReleasesEvent {
    data object OnTryAgainClick : ReleasesEvent
}
