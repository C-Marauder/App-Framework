package com.hd.framework.list

import android.Manifest
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hd.framework.R
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.ui.ModuleHostActivity
import com.xhh.ui.result.ResultCaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */
class ListPage:ModuleHostActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_list)

        getResultCaller().register<HashMap<String,Boolean>>(ResultCaller.PERMISSION_MULTI){
            Log.e("===","===$it")
        }

        getResultCaller().request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
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