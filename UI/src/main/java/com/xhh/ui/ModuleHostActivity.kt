package com.xhh.ui

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.xhh.ui.fragment.CoreFragment
import com.xhh.ui.result.ResultCaller
import java.io.Serializable

/**
 *   @Author:小灰灰
 *   @Time:2020/10/30
 *   @Desc:
 */
abstract class ModuleHostActivity: AppCompatActivity() {

    private lateinit var mHomeFragment:Fragment
    private lateinit var mResultCaller: ResultCaller

    protected fun getResultCaller(): ResultCaller {
        if (!::mResultCaller.isInitialized){
            mResultCaller = ResultCaller.create(this)
        }
        return mResultCaller
    }

    abstract fun onCreateHostFragment(intent: Intent):Fragment
    abstract fun onCreateFragments(tag:String):Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeFragment = onCreateHostFragment(intent)
        supportFragmentManager.commit {
            replace(android.R.id.content,mHomeFragment)
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks(){

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                if (f != mHomeFragment && !f.isRemoving){
                    supportFragmentManager.commitNow {
                        remove(f)
                    }
                }
            }
        },true)

    }

    fun navigate(tag: String, vararg params:Pair<String,Any>){
        val fragment = onCreateFragments(tag)
        if (!params.isNullOrEmpty()) {
            fragment.arguments = Bundle().apply {
                params.forEach {
                    val key = it.first
                    when (val value = it.second) {
                        is String -> putString(key, value)
                        is Parcelable -> putParcelable(key, value)
                        is Serializable-> putSerializable(key,value)
                        is Int -> putInt(key, value)
                        is Boolean -> putBoolean(key, value)
                    }
                }
            }
        }
        val currentFragment = supportFragmentManager.fragments.findLast {
            it is CoreFragment
        } as Fragment
        supportFragmentManager.commit {
            add(android.R.id.content,fragment)
            hide(currentFragment)
            show(fragment)
            setReorderingAllowed(true)
            addToBackStack(tag)

        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size == 1){
            finish()
        }else{
            super.onBackPressed()
        }

    }


}