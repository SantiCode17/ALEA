package com.example.alea.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(
    private val context: Context
) {
    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_IS_DEMO_MODE = booleanPreferencesKey("is_demo_mode")
        private val KEY_THEME_COLOR = intPreferencesKey("theme_color")

        // Default theme color (Orange - alea_theme_orange)
        const val DEFAULT_THEME_COLOR = 0xFFFF8C42.toInt()
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_LOGGED_IN] ?: false
    }

    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    val isDemoMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_IS_DEMO_MODE] ?: false
    }

    val themeColor: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_THEME_COLOR] ?: DEFAULT_THEME_COLOR
    }

    suspend fun saveSession(userId: String, isDemoMode: Boolean = false) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_IS_LOGGED_IN] = true
            preferences[KEY_IS_DEMO_MODE] = isDemoMode
        }
    }

    suspend fun setThemeColor(color: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_COLOR] = color
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_USER_ID)
            preferences[KEY_IS_LOGGED_IN] = false
            preferences[KEY_IS_DEMO_MODE] = false
        }
    }

    suspend fun isLoggedInSync(): Boolean {
        return context.dataStore.data.first()[KEY_IS_LOGGED_IN] ?: false
    }

    suspend fun getUserIdSync(): String? {
        return context.dataStore.data.first()[KEY_USER_ID]
    }

    suspend fun getThemeColorSync(): Int {
        return context.dataStore.data.first()[KEY_THEME_COLOR] ?: DEFAULT_THEME_COLOR
    }
}
