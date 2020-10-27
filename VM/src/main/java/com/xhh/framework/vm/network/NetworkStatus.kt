package com.xhh.framework.vm.network

/**
 *   @Author:小灰灰
 *   @Time:2020/10/24
 *   @Desc:
 */
interface NetworkStatus {

    companion object{
        const val CONNECTED:Int = 0
        const val NONE:Int = 1
        const val LOST:Int = 2

    }
}