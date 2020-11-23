package com.xhh.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.databinding.adapters.ViewBindingAdapter
import androidx.fragment.app.Fragment
import com.xhh.ui.ModuleHostActivity
import com.xhh.ui.result.ResultCaller

/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
 abstract class CoreFragment:Fragment() {
    private lateinit var moduleHostActivity: ModuleHostActivity
    private lateinit var mResultCaller: ResultCaller

    protected fun getResultCaller():ResultCaller{
         if (!::mResultCaller.isInitialized){
            mResultCaller = ResultCaller.create(this)
        }
        return mResultCaller
    }

}

