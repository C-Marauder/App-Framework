package com.xhh.ui.fragment

import android.content.Context
import android.os.Bundle
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
//        moduleHostActivity = context as ModuleHostActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    protected fun getResultCaller():ResultCaller{
         if (!::mResultCaller.isInitialized){
            mResultCaller = ResultCaller.create(this)
        }
        return mResultCaller
    }

}

