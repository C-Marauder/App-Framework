package com.xhh.framework.vm.http

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.xhh.framework.vm.http.call.CallToAdapterFactory
import com.xhh.framework.vm.http.converter.ResourceConverterFactory
import okhttp3.Cache
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.net.ssl.SSLSocketFactory


/**
 *   @Author:小灰灰
 *   @Time:2020/10/24
 *   @Desc:
 */
class ApiHelper<T> private constructor(
    private val app: Application,
    service: Class<T>,
    baseUrl: String,
    private val interceptorOk: (builder: OkHttpClient.Builder) -> Unit,
    private val process: (result: String, adapter: TypeAdapter<*>, gson: Gson) -> Resource<*>

    ) {
    companion object {
        private const val TAG: String = "http-log"

        fun <T> create(app: Application, service: Class<T>, baseUrl: String,interceptorOk:(builder:OkHttpClient.Builder)->Unit,process: (result:String,adapter:TypeAdapter<*>,gson:Gson)->Resource<*>): T {
            return ApiHelper(app, service, baseUrl, interceptorOk,process).api
        }
    }

    private var api: T

    private val okHttpClient: OkHttpClient by lazy {
        with(OkHttpClient.Builder()) {
            interceptorOk(this)
            addInterceptor(HttpLoggingInterceptor {
                Log.e(TAG, it)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            cache(Cache(app.externalCacheDir ?: app.cacheDir, 10 * 1024 * 1024))
            build()
        }
    }

    init {

        api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ResourceConverterFactory { result, adapter, gson ->
                process(result,adapter,gson)
//                val jsonElement = JsonParser.parseString(result)
//                if (jsonElement.isJsonObject) {
//                    val jsonObject = jsonElement.asJsonObject
//                    val code = jsonObject[codeKey]
//                    val realCode = try {
//                        code.asString
//                    } catch (e: UnsupportedOperationException) {
//                        "${code.asInt}"
//                    }
//                    val message = jsonObject[messageKey].asString
//                    if (realCode == successCode) {
//                        val data = with(jsonObject[dataKey]) {
//                            when {
//                                isJsonObject -> {
//                                    asJsonObject
//                                }
//                                isJsonArray -> {
//                                    asJsonArray
//                                }
//
//                                else -> {
//                                    asJsonNull
//                                }
//                            }
//                        }
//                        Resource.success(adapter.fromJson(gson.toJson(data)))
//                    } else {
//                        Resource.error(realCode, message)
//                    }
//                } else {
//                    Resource.error<Any>("200", "body is not a json object")
//                }
            })
            .addCallAdapterFactory(CallToAdapterFactory())
            .build()
            .create(service)

    }
}