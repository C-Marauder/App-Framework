package com.xhh.ui.utils

import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.xhh.ui.App

/**
 *   @Author:小灰灰
 *   @Time:2020/11/1
 *   @Desc:
 */

val  Int.dp :Float get() {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this*1f,App.instance.resources.displayMetrics)
}

val Int.px:Float get() {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,this*1f,App.instance.resources.displayMetrics)
}
val Int.sp:Float get() {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,this*1f,App.instance.resources.displayMetrics)
}

inline fun <reified T:AppCompatActivity> T.snack(msg:String,noinline init:(Snackbar.()->Unit)?=null,noinline onDismiss:(()->Unit)?=null){
    Snackbar.make(findViewById(android.R.id.content),msg,Snackbar.LENGTH_LONG).apply {
        init?.invoke(this)
        onDismiss?.let {
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    it.invoke()
                }
            })
        }

    }.show()
}