package com.xhh.framework.vm.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
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
inline fun <reified T:AppViewModel> LifecycleOwner.getViewModel():T{
    return when(this){
        is Fragment-> ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(T::class.java)
        is AppCompatActivity -> ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(T::class.java)
        else -> throw Exception("")
    }
}


fun AppViewModel.registerEvent(lifecycleOwner: LifecycleOwner, onReceiver:(event:String)->Unit){
    mMessageObserver.observe(lifecycleOwner){
        onReceiver(it)
    }

}