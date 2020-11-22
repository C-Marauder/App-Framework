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
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.framework.vm.utils.getViewModel
import com.xhh.framework.vm.utils.register
import com.xhh.ui.rv.ItemAdapter
import com.xhh.ui.rv.init
import com.xhh.ui.utils.dp
import kotlinx.android.synthetic.main.page_list.*

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */
class ListPage:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.page_list)
        val items = mutableListOf<String>()
        for (i in 1..24){
            items.add("$i")
        }
        val listAdapter =  ItemAdapter<String,ItemListBinding>(items,onCreateItemLayout = {
            R.layout.item_list
        },onViewHolderCreated = {
            holder, dataBinding ->
        },onItemBind = {
            dataBinding, position ->
            dataBinding.content = items[position]
        })
        val loadAdapter = ItemAdapter<String,ItemLoadingBinding>(mutableListOf("1"),onCreateItemLayout = {
            R.layout.item_loading
        },onViewHolderCreated = {
            holder, dataBinding ->
        },onItemBind = {
            dataBinding, position ->

        })

        listView.recyclerView.init(onBindAdapter = {
            adapter = ConcatAdapter(listAdapter,loadAdapter)
        },divider = GradientDrawable().apply {
            setColor(Color.parseColor("#f1f1f1"))
            setSize(-1, 1.dp.toInt())

        }){
            Log.e("===","====$it")
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