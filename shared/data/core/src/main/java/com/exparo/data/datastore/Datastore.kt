package com.exparo.data.datastore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

typealias ExparoDataStore = DataStore<Preferences>

val Context.dataStore: ExparoDataStore by preferencesDataStore(
    name = "exparo_wallet_datastore_v1"
)

@Composable
fun datastore(): ExparoDataStore {
    return LocalContext.current.dataStore
}
