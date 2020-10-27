package com.xhh.framework.vm.network

/**
 *   @Author:小灰灰
 *   @Time:2020/10/25
 *   @Desc:
 */
interface OnNetworkStatusChangeListener {

    fun onChanged(isConnected:Boolean,status:Int)
}