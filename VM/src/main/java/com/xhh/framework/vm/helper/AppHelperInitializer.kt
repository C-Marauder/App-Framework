package com.xhh.framework.vm.helper

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

/**
 *   @Author:小灰灰
 *   @Time:2020/10/25
 *   @Desc:
 */
internal class AppHelperInitializer: Initializer<AppHelper> {
    override fun create(context: Context): AppHelper {
        AppHelper.init(context as Application)
        return AppHelper
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}