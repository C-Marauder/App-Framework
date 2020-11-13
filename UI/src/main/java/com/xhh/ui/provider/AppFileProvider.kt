package com.xhh.ui.provider

import android.app.Application
import androidx.core.content.FileProvider
import com.xhh.ui.App

/**
 *   @Author:小灰灰
 *   @Time:2020/11/1
 *   @Desc:
 */
internal class AppFileProvider :FileProvider(){
    override fun onCreate(): Boolean {
        val isCreated = super.onCreate()
        context?.let {
            App.instance = it as Application
        }
        return isCreated
    }
}