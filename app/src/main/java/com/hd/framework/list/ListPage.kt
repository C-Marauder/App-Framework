package com.hd.framework.list

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.shape.MaterialShapeDrawable
import com.hd.framework.R
import com.hd.framework.databinding.ItemListBinding
import com.hd.framework.databinding.ItemLoadingBinding
import com.xhh.ui.utils.dp
import kotlinx.android.synthetic.main.page_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.framework.vm.utils.getViewModel
import com.xhh.framework.vm.utils.register
import com.xhh.ui.rv.ItemAdapter
import com.xhh.ui.rv.init
import com.xhh.ui.utils.dp
import kotlinx.android.synthetic.main.page_list.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */
class ListPage:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_list)
        lifecycleScope.async (Dispatchers.IO){
            for (i in 1..10){
                async {
                    while (isActive){
                        delay(1000)
                        Log.e("===$i","====》》》")
                    }
                }
            }
        }
    }


}
class MyViewModel(application: Application) : AppViewModel(application) {
    override fun onDispatchService(service: String): Resource<*> {
        return Resource.success(service)
    }

    override fun onCallRequest(service: String) {
        requestAsync<String>(service)
    }

    override fun disConnected() {

    }

}