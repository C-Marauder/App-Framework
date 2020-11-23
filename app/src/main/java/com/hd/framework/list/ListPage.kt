package com.hd.framework.list

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hd.framework.R
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

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