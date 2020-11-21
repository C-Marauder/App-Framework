package com.xhh.framework.vm.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData


/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
internal class ResourceLiveData<T>: MutableLiveData<ResourceStatus<T>>() {

    fun loading(){
        postValue(ResourceStatus.Loading())
    }

    fun success(data: T?){
        postValue(ResourceStatus.Success(data))
    }

    fun error(code: String,message:String){
        postValue(ResourceStatus.Error(code,message))
    }
    fun observer(lifecycleOwner: LifecycleOwner,onLoading:()->Unit,onSuccess:(data:T?)->Unit,onError:(code:String,msg:String)->Unit){
        observe(lifecycleOwner){
            when(it.status){
                ResourceStatus.LOADING-> onLoading()
                ResourceStatus.SUCCESS-> onSuccess(it.data)
                ResourceStatus.ERROR->onError(it.code!!,it.message!!)

            }
        }
    }
}