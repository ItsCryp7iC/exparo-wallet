package com.exparo.wallet.migrations

interface Migration {
    val key: String

    suspend fun migrate()
}
