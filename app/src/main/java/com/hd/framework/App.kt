package com.hd.framework

import android.app.Application
import com.google.firebase.FirebaseApp
import com.xhh.framework.vm.helper.AppHelper
import com.xhh.framework.vm.http.ApiHelper
import okhttp3.Headers
import kotlin.properties.Delegates

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
class App:Application() {
    companion object{
        var api:AppService by Delegates.notNull<AppService>()
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

    }
}