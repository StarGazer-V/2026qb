package com.medgttracker.auth

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionStore by preferencesDataStore(name = "session")

@Singleton
class SessionPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val stickyLoginKey = booleanPreferencesKey("sticky_login")
    private val userIdKey = stringPreferencesKey("user_id")

    val stickyUserId: Flow<String?> = context.sessionStore.data.map { prefs ->
        if (prefs[stickyLoginKey] == true) prefs[userIdKey] else null
    }

    suspend fun markLoggedIn(userId: String) {
        context.sessionStore.edit { prefs ->
            prefs[stickyLoginKey] = true
            prefs[userIdKey] = userId
        }
    }

    suspend fun clear() {
        context.sessionStore.edit { it.clear() }
    }
}
