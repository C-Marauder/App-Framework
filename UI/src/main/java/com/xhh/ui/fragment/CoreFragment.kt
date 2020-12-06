package com.xhh.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.databinding.adapters.ViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.xhh.ui.ModuleHostActivity
import com.xhh.ui.generated.callback.OnClickListener
import com.xhh.ui.result.ResultCaller

/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
 abstract class CoreFragment:Fragment(), Transition.TransitionListener,View.OnClickListener {
    private lateinit var moduleHostActivity: ModuleHostActivity
    private lateinit var mResultCaller: ResultCaller
    private val mTransitionInflater: TransitionInflater by lazy {
        TransitionInflater.from(requireContext())
    }
    protected fun getResultCaller():ResultCaller{
         if (!::mResultCaller.isInitialized){
            mResultCaller = ResultCaller.create(this)
        }
        return mResultCaller
    }
    private val mTransition:Transition by lazy {
        mTransitionInflater.inflateTransition(android.R.transition.slide_right)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ModuleHostActivity){
            moduleHostActivity = context
        }

    }
    abstract fun onViewRealCreated()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = mTransitionInflater.inflateTransition(android.R.transition.slide_left)
        mTransition.addListener(this)
        enterTransition = mTransition


    }

    override fun onDetach() {
        super.onDetach()
        mTransition.removeListener(this)

    }
    private  var mCurrentClickView:View?=null
    abstract fun onStartPageClick(v: View?)
    override fun onClick(v: View?) {
        this.mCurrentClickView = v
        this.mCurrentClickView?.isEnabled = false
        onStartPageClick(v)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            mCurrentClickView?.isEnabled = true
        }
    }

    protected fun navigate(tag:String,vararg params:Pair<String,Any>){
        moduleHostActivity.navigate(tag,*params)
    }

    override fun onTransitionStart(transition: Transition) {

    }

    override fun onTransitionEnd(transition: Transition) {
        onViewRealCreated()
    }

    override fun onTransitionCancel(transition: Transition) {

    }

    override fun onTransitionPause(transition: Transition) {

    }

    override fun onTransitionResume(transition: Transition) {

    }

}

