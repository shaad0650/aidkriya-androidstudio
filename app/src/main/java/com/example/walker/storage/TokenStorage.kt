package com.example.walker.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to instantiate DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

class TokenStorage(private val context: Context) {

    companion object {
        // Define the key for storing the access token
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    /**
     * Saves the access token to DataStore.
     * This is a suspend function as DataStore operations are asynchronous.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    /**
     * Retrieves the access token from DataStore as a Flow.
     */
    val getToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    /**
     * Clears the access token from DataStore.
     */
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}
