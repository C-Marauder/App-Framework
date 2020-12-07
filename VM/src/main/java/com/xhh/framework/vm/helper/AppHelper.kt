package com.xhh.framework.vm.helper

import android.app.Application
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.xhh.framework.vm.network.AppNetworkObserver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 *   @Author:小灰灰
 *   @Time:2020/10/25
 *   @Desc:
 */
object AppHelper {
    private lateinit var mAppNetworkObserver: AppNetworkObserver
    lateinit var application: Application
    private val dataStore: DataStore<Preferences> by lazy {
        application.createDataStore("settings")
    }
    private lateinit var encryptedSharedPreferences: SharedPreferences
    private lateinit var mPackageInfo: PackageInfo
    val isOnline: Boolean
        get() {
            return mAppNetworkObserver.isOnline
        }
    val version: String
        get() {
            return mPackageInfo.versionName
        }
    val versionCode: String
        get() {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                "${mPackageInfo.longVersionCode}"
            } else {
                "${mPackageInfo.versionCode}"
            }
        }

    internal fun init(application: Application) {
        this.application = application
        val masterKey = MasterKey.Builder(application, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            application,
            "app_encryred_config",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        mPackageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
        mAppNetworkObserver = AppNetworkObserver(application)
    }

    suspend fun saveConfigs(vararg configs: Pair<String, Any>) {
        if (!configs.isNullOrEmpty()) {
            dataStore.edit { settings ->
                configs.forEach {
                    val key = it.first
                    when (val value = it.second) {
                        is Int -> settings[preferencesKey<Int>(key)] = value
                        is String -> settings[preferencesKey<String>(key)] = value
                        is Boolean -> settings[preferencesKey<Boolean>(key)] = value
                        is Float -> settings[preferencesKey<Float>(key)] = value
                        is Long -> settings[preferencesKey<Long>(key)] = value
                        is Double -> settings[preferencesKey<Double>(key)] = value
                        else -> throw Exception("")
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> getConfig(key: String,defaultValue:T): T {
        return dataStore.data.map { preferences ->
            val preferencesKey = when (defaultValue) {
                is Int -> preferencesKey<Int>(key)
                is String -> preferencesKey<String>(key)
                is Float -> preferencesKey<Float>(key)
                is Long -> preferencesKey<Long>(key)
                is Double -> preferencesKey<Double>(key)
                is Boolean -> preferencesKey<Boolean>(key)
                else -> throw Exception("")
            }
            preferences[preferencesKey] ?: defaultValue
        }.first() as T

    }

    fun write(vararg params: Pair<String, Any>) {
        if (!params.isNullOrEmpty()) {
            encryptedSharedPreferences.edit {
                params.forEach {
                    val key = it.first
                    when (val value = it.second) {
                        is String -> putString(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Int -> putInt(key, value)
                        is Float -> putFloat(key, value)
                    }

                }
            }

        }

    }

    @Suppress("UNCHECKED_CAST")
    fun <T> read(key: String, default: T): T {
        return when (default) {
            is String -> encryptedSharedPreferences.getString(key, default)
            is Boolean -> encryptedSharedPreferences.getBoolean(key, default)
            is Float -> encryptedSharedPreferences.getFloat(key, default)
            is Int -> encryptedSharedPreferences.getInt(key, default)
            else -> throw Exception("")
        } as T


    }


}

