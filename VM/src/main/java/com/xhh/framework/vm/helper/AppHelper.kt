package com.xhh.framework.vm.helper

import android.app.Application
import com.xhh.framework.vm.network.AppNetworkObserver

/**
 *   @Author:小灰灰
 *   @Time:2020/10/25
 *   @Desc:
 */
object AppHelper {
    private lateinit var mAppNetworkObserver: AppNetworkObserver
    lateinit var application: Application
    val isOnline:Boolean get() {
        return mAppNetworkObserver.isOnline
    }

    internal fun init(application: Application){
        this.application = application
        mAppNetworkObserver = AppNetworkObserver(application)
    }
}