package com.xhh.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xhh.ui.result.ResultCaller

/**
 *   @Author:小灰灰
 *   @Time:2020/10/30
 *   @Desc:
 */
abstract class ModuleHostActivity: AppCompatActivity() {

    private lateinit var mCurrentFragment: Fragment
    private lateinit var mHomeFragment:Fragment
    private lateinit var mResultCaller: ResultCaller

    protected fun getResultCaller(): ResultCaller {
        if (!::mResultCaller.isInitialized){
            mResultCaller = ResultCaller.create(this)
        }
        return mResultCaller
    }
//    abstract fun onCreateFragment(tag:String):Fragment
//    abstract fun onCreateHomeFragment(intent:Intent):Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mHomeFragment = onCreateHomeFragment(intent)
//        supportFragmentManager.commit {
//            add(android.R.id.content,mHomeFragment)
//
//        }
//        supportFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks(){
//            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
//                super.onFragmentResumed(fm, f)
//                mCurrentFragment = f
//            }
//
//            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
//                supportFragmentManager.commit {
//                    remove(f)
//                }
//                super.onFragmentDestroyed(fm, f)
//            }
//        },false)

    }

//    fun startFragment(tag: String, vararg params:Pair<String,Any>){
//        if (tag == mCurrentFragment.tag){
//            return
//        }
//        val fragment = onCreateFragment(tag)
//        if (!params.isNullOrEmpty()) {
//            fragment.arguments = Bundle().apply {
//                params.forEach {
//                    val key = it.first
//                    when (val value = it.second) {
//                        is String -> putString(key, value)
//                        is Parcelable -> putParcelable(key, value)
//                        is Serializable-> putSerializable(key,value)
//                        is Int -> putInt(key, value)
//                        is Boolean -> putBoolean(key, value)
//                    }
//                }
//            }
//        }
//        supportFragmentManager.commit {
//            fragment.enterTransition = Slide(Gravity.RIGHT)
//            //fragment.exitTransition = Slide(Gravity.LEFT)
//            mCurrentFragment.exitTransition =Slide(Gravity.RIGHT)
////            mCurrentFragment.enterTransition = Slide(Gravity.LEFT)
//            add(android.R.id.content,fragment)
//            hide(mCurrentFragment)
//            show(fragment)
//            setReorderingAllowed(true)
//            addToBackStack(tag)
//
//        }
//    }

//    override fun onBackPressed() {
//        if (mCurrentFragment == mHomeFragment){
//            finish()
//        }else{
//            super.onBackPressed()
//        }
//
//    }


}