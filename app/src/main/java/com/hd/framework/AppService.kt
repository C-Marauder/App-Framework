package com.hd.framework

import com.hd.framework.model.City
import com.hd.framework.model.House
import com.xhh.framework.vm.http.Resource
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
interface AppService {

    @GET("project/second/areas")
    fun getMyHouse(@Query("areaName")areaName:String?=null):Resource<City>
}