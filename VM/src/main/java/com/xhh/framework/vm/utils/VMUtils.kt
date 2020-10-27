package com.xhh.framework.vm.utils

import androidx.lifecycle.LifecycleOwner
import com.xhh.framework.vm.AppViewModel

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */


fun <T> AppViewModel.register(service: String,lifecycleOwner: LifecycleOwner,onLoading:()->Unit,
                              onSuccess:(data:T?)->Unit,onError:(code:String,msg:String)->Unit){
    this.getResourceLiveData<T>(service).observer(lifecycleOwner,onLoading,onSuccess,onError)
}