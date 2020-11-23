package com.xhh.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *   @Author:小灰灰
 *   @Time:2020/11/15
 *   @Desc:
 */
class ItemAdapter<T,DB:ViewDataBinding>(
    private val data:T?=null,
    private val dataList:MutableList<T>?=null,
    private val onCreateItemLayout: (position: Int) -> Int,
    private val onViewHolderCreated: (holder:RecyclerView.ViewHolder,dataBinding:DB) ->Unit,
    private val onItemBind:(dataBinding:DB,position:Int)->Unit
) : RecyclerView.Adapter<ItemViewHolder<DB>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<DB> {
        val dataBinding = DataBindingUtil.inflate<DB>(
            LayoutInflater.from(parent.context),
            viewType, parent, false)
        dataBinding.lifecycleOwner = parent.context as AppCompatActivity
        return ItemViewHolder(dataBinding.root,{
            onViewHolderCreated(it,dataBinding)
        }){
            onItemBind(dataBinding,it)
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<DB>, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return onCreateItemLayout(position)
    }

    override fun getItemCount(): Int = if (data == null){
        dataList!!.size
    }else{
        1
    }
}


class ItemViewHolder<DB:ViewDataBinding>(itemView: View, onCreated:(holder: ItemViewHolder<DB>)->Unit,private val onItemBind: (position:Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
    init {
        onCreated(this)
    }

    internal fun onBind(position: Int){
        onItemBind(position)
    }
}