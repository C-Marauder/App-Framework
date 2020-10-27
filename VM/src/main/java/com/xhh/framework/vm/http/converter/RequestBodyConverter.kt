package com.xhh.framework.vm.http.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class RequestBodyConverter<T>(private val isUploadFile:Boolean, private val gson: Gson, private val adapter: TypeAdapter<T>):
    Converter<T, RequestBody> {
    companion object{
        private val MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8")
        private val FILE_TYPE = MediaType.get("multipart/form-data; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        val type = if (isUploadFile){
            FILE_TYPE
        }else{
            MEDIA_TYPE
        }
        return RequestBody.create(type, buffer.readByteString())
    }

}