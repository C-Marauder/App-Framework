package com.xhh.framework.vm.http.call

import com.xhh.framework.vm.http.Resource
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class CallToAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val mClz = getRawType(returnType)
        if (mClz != Resource::class.java){
            return null
        }
        if (returnType !is ParameterizedType){
            return null
        }
        val responseType = getParameterUpperBound(0, returnType)
        return CallToResourceAdapter<Any>(responseType)
    }
}