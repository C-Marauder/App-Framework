package com.xhh.framework.vm.http.call

import android.util.Log
import com.xhh.framework.vm.http.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class CallToResourceAdapter<T>(private val responseType: Type) :
    CallAdapter<T, Resource<T>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Resource<T> {
        return call.execute().body() as Resource<T>

    }
}