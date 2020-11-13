package com.hd.framework.fragmenttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hd.framework.R

/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
class FirstFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_first,container,false).apply {
            setOnClickListener {
            }
        }
    }
}
class SecondFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_second,container,false).apply {

        }
    }
}