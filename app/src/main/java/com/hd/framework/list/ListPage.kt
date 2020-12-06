package com.hd.framework.list

import android.Manifest
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.hd.framework.R
import com.hd.framework.databinding.ItemListBinding
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.ui.ModuleHostActivity
import com.xhh.ui.result.ResultCaller
import com.xhh.ui.rv.ItemAdapter
import com.xhh.ui.rv.initRV
import kotlinx.android.synthetic.main.page_list.*
import kotlinx.coroutines.*

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */
class ListPage:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_list)
        val dataList = mutableListOf<String>()
        for (i in 1..50){
            dataList.add("$i")
        }
        listView.set {
            initRV(onBindAdapter = {
                ItemAdapter<String,ItemListBinding>(dataList = dataList,onCreateItemLayout = {
                    R.layout.item_list
                },onViewHolderCreated = {
                    holder, dataBinding ->
                },onItemBind = {
                    dataBinding, position ->
                    dataBinding.content = dataList[position]
                })

            })


        }.setRefreshListener {
            lifecycleScope.launch {
                delay(5000)
                it.value = true
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