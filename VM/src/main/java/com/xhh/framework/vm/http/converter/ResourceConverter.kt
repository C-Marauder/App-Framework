package com.xhh.framework.vm.http.converter

import com.xhh.framework.vm.http.Resource
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class ResourceConverter<T>(private val process:(result:String)->Resource<T>) : Converter<ResponseBody,Resource<T>> {
    override fun convert(value: ResponseBody): Resource<T>? {
        return try {
            process(value.string())
        }catch (e:Exception){
            e.printStackTrace()
            Resource.error("-404",e.message)
        }
    }
}