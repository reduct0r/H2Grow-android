package com.h2grow.app.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val JWT_ACCESS_TOKEN = stringPreferencesKey("JWT_ACCESS_TOKEN")
    val JWT_REFRESH_TOKEN = stringPreferencesKey("JWT_REFRESH_TOKEN")
}