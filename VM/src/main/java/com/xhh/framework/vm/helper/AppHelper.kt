package com.xhh.framework.vm.helper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.security.keystore.KeyGenParameterSpec
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.*
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.xhh.framework.vm.BuildConfig
import com.xhh.framework.vm.network.AppNetworkObserver
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalArgumentException

/**
 *   @Author:小灰灰
 *   @Time:2020/10/25
 *   @Desc:
 */
object AppHelper {
    private lateinit var mAppNetworkObserver: AppNetworkObserver
    lateinit var application: Application
    lateinit var sp: DataStore<Preferences>
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
        sp = application.createDataStore(name = "app_config")

        val masterKey = MasterKey.Builder(application,MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        encryptedSharedPreferences = EncryptedSharedPreferences.create(application,"app_encryred_config",masterKey,EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        mPackageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
        mAppNetworkObserver = AppNetworkObserver(application)
    }
    fun write(vararg params:Pair<String,Any>){
        if (!params.isNullOrEmpty()){
            encryptedSharedPreferences.edit {
                params.forEach {
                    val key = it.first
                    when (val value = it.second){
                        is String-> putString(key,value)
                        is Boolean ->putBoolean(key,value)
                        is Int ->putInt(key,value)
                        is Float-> putFloat(key,value)
                    }

                }
            }

        }

    }

    @Suppress("UNCHECKED_CAST")
    fun <T>read(key: String, default:T):T{
        return when(default){
            is String->encryptedSharedPreferences.getString(key,default)
            is Boolean-> encryptedSharedPreferences.getBoolean(key,default)
            is Float-> encryptedSharedPreferences.getFloat(key,default)
            is Int-> encryptedSharedPreferences.getInt(key,default)
            else-> throw Exception("")
        } as T


    }
    suspend inline fun <reified T:Any> write(key:String,value:T){
        sp.edit {
            it[preferencesKey<T>(key)]  = value
        }

    }

    @Suppress("UNCHECKED_CAST")
    suspend inline fun<reified T:Any>  read(key: String):T?{
        return sp.data.map {
            it[preferencesKey<T>(key)]
        }.first()

    }



}

