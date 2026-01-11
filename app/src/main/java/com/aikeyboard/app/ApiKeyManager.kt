package com.aikeyboard.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ApiKeyManager(private val context: Context) {
    
    private companion object {
        val API_KEY = stringPreferencesKey("api_key")
    }
    
    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[API_KEY] = key
        }
    }
    
    suspend fun getApiKey(): String {
        return context.dataStore.data.map { preferences ->
            preferences[API_KEY] ?: ""
        }.first()
    }
    
    suspend fun hasApiKey(): Boolean {
        return getApiKey().isNotEmpty()
    }
}
