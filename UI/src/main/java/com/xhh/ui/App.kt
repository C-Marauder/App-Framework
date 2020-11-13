package com.xhh.ui

import android.app.Application
import kotlin.properties.Delegates

/**
 *   @Author:小灰灰
 *   @Time:2020/11/1
 *   @Desc:
 */
internal object App{
    var instance:Application by Delegates.notNull<Application>()
}