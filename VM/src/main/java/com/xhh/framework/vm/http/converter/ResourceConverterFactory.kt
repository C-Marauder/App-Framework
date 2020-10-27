package com.xhh.framework.vm.http.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.xhh.framework.vm.http.Resource
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class ResourceConverterFactory(private val process:(result:String,adapter:TypeAdapter<*>,gson:Gson)->Resource<*>): Converter.Factory() {

    private val gson : Gson by lazy {
        Gson()
    }
    private lateinit var adapter: TypeAdapter<*>
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val isUploadFile= methodAnnotations.any {
            it::class.java.simpleName == "Multipart"
        }
        adapter = gson.getAdapter(TypeToken.get(type))
        return RequestBodyConverter(isUploadFile,gson,adapter)
    }
    @Suppress("UNCHECKED_CAST")
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return ResourceConverter{
            process(it,adapter,gson)
        }
    }
}