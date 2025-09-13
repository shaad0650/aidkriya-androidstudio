package com.example.walker.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension property to easily access DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class TokenStorage(private val context: Context) {
    companion object {
        private val ACCESS_KEY = stringPreferencesKey("jwt_access")
        private val REFRESH_KEY = stringPreferencesKey("jwt_refresh")
    }

    // Save access & refresh token
    suspend fun saveTokens(access: String, refresh: String? = null) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_KEY] = access
            refresh?.let { prefs[REFRESH_KEY] = it }
        }
    }

    // Save only access token (if refresh unchanged)
    suspend fun saveAccessToken(access: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_KEY] = access
        }
    }

    // Flow of access token (UI can observe changes)
    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ACCESS_KEY]
    }

    // Flow of refresh token
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[REFRESH_KEY]
    }

    // Load once (blocking)
    suspend fun loadAccessToken(): String? = accessTokenFlow.first()
    suspend fun loadRefreshToken(): String? = refreshTokenFlow.first()

    // Quick check
    suspend fun hasToken(): Boolean = loadAccessToken() != null

    // Clear DataStore (logout)
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
