package com.wildantechnoart.frontendcodingtes

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MyApp : Application() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

    suspend fun saveStringDataStore(context: Context, key: String, value: String?) =
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it[stringPreferencesKey(key)] = value.toString()
            }
        }

    suspend fun clearDataStore(context: Context) =
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it.clear()
            }
        }

    suspend fun readStringDataStore(context: Context, key: String): String? = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[stringPreferencesKey(key)] }
        .flowOn(Dispatchers.IO)
        .first()

    companion object {
        private var instance: MyApp? = null

        @Synchronized
        fun getInstance(): MyApp {
            if (instance == null) {
                instance = MyApp()
            }
            return instance as MyApp
        }
    }

}