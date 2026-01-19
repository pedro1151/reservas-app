package com.optic.pramosfootballappz.data.dataSource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.optic.pramosfootballappz.core.Config.AUTH_KEY
import com.optic.pramosfootballappz.domain.model.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthDatastore constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun save(authResponse: AuthResponse) {
        val dataStoreKey = stringPreferencesKey(AUTH_KEY)
        dataStore.edit { pref ->
            pref[dataStoreKey] = authResponse.toJson()
        }
    }



    suspend fun delete() {
        val dataStoreKey = stringPreferencesKey(AUTH_KEY)
        dataStore.edit { pref ->
            pref.remove(dataStoreKey)
        }
    }

    fun getData(): Flow<AuthResponse> {
        val dataStoreKey = stringPreferencesKey(AUTH_KEY)
        return dataStore.data.map { pref ->
            if (pref[dataStoreKey] != null) {
                AuthResponse.fromJson(pref[dataStoreKey]!!)
            }
            else {
                AuthResponse()
            }
        }
    }


    /* languages */

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    val languageFlow: Flow<String> =
        dataStore.data.map { it[LANGUAGE_KEY] ?: "es" }

    suspend fun setLanguage(language: String) {
        dataStore.edit {
            it[LANGUAGE_KEY] = language
        }

    }

    /** ✅ ESTA ES LA FUNCIÓN QUE FALTABA */
    suspend fun getSavedLanguage(): String {
        return dataStore.data.first()[LANGUAGE_KEY] ?: "es"
    }




}