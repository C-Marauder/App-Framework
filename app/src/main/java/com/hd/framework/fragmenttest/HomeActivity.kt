package com.hd.framework.fragmenttest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xhh.ui.ModuleHostActivity

/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getPermissionHelper()
    }
//    override fun onCreateFragment(tag: String): Fragment {
//        return when(tag){
//            "1"-> FirstFragment()
//            "2"->SecondFragment()
//            else -> throw Exception()
//        }
//    }
//
//    override fun onCreateHomeFragment(intent: Intent): Fragment {
//        return FirstFragment()
//    }
}