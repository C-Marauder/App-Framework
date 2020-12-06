package com.xhh.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.XmlRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout
import com.xhh.ui.R

/**
 *   @Author:小灰灰
 *   @Time:2020/11/4
 *   @Desc:
 */
abstract class DataBindingFragment<T:ViewDataBinding>: CoreFragment() {
    companion object Style {
        const val COORDINATOR: Int = 1
        const val CUSTOMER:Int  =2
    }

    abstract val layoutId: Int
    open val fragmentStyle:Int = COORDINATOR

    abstract fun onDataBindingCreated(dataBinding: ViewDataBinding)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return when (fragmentStyle) {
            COORDINATOR -> {

                val mCoordinatorLayout = inflater.inflate(R.layout.f_coordinator, container, false) as CoordinatorLayout
                val mDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, mCoordinatorLayout, false)
                mDataBinding.lifecycleOwner = this
                onDataBindingCreated(mDataBinding)
                mCoordinatorLayout.addView(mDataBinding.root,CoordinatorLayout.LayoutParams(-1,1).apply {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                })
                mCoordinatorLayout
            }
            CUSTOMER -> {
                val mDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater,layoutId,container,false)
                onDataBindingCreated(mDataBinding)
                mDataBinding.root

            }

            else -> throw Exception("")
        }
    }


}

