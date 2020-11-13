package com.xhh.framework.vm

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xhh.framework.vm.helper.AppHelper
import com.xhh.framework.vm.http.Resource
import com.xhh.framework.vm.livedata.ResourceLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 *   @Author:小灰灰
 *   @Time:2020/10/24
 *   @Desc:
 */
abstract class AppViewModel(application: Application) : AndroidViewModel(application),Observable {
    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    internal val mMessageObserver:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val mResourceCallbacks:ConcurrentHashMap<String, ResourceLiveData<*>> by lazy {
        ConcurrentHashMap<String, ResourceLiveData<*>>()
    }
    @get:Bindable
    var service:String?=null
    set(value) {
        field = value
        notifyPropertyChanged(BR.service)
    }
    init {
        addOnPropertyChangedCallback(object :Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {

                if (!service.isNullOrEmpty() && propertyId == BR.service){
                    onCallRequest(service!!)
                }
            }

        })
    }

    fun updateUIByEvent(event:String){
        mMessageObserver.value = event
    }
    open fun load(service: String){
        this.service = service
    }
    @Suppress("UNCHECKED_CAST")
    internal fun <T>getResourceLiveData(service: String):ResourceLiveData<T>{
        var resourceStatus = mResourceCallbacks[service]
        if (resourceStatus == null){
            resourceStatus = ResourceLiveData<T>()
            mResourceCallbacks[service] = resourceStatus
        }
        return resourceStatus as ResourceLiveData<T>
    }
    protected abstract fun onDispatchService(service: String):Resource<*>
    protected abstract fun onCallRequest(service: String)
    abstract fun disConnected()
    protected fun <T>request(service: String){
        if (AppHelper.isOnline){
            viewModelScope.launch {
                load<T>(service)
            }
        }else{
            disConnected()
        }

    }

    protected fun <T>requestAsync(service: String){
        if (AppHelper.isOnline){
            viewModelScope.async {
                load<T>(service)
            }
        }else{
            disConnected()
        }

    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T>load(service: String){
        val resourceLiveData = getResourceLiveData<T>(service)
        resourceLiveData.loading()
        withContext(viewModelScope.coroutineContext + Dispatchers.IO){
            when(val resource = onDispatchService(service)){
                is Resource.Success -> resourceLiveData.success(resource.data as T)
                is Resource.Error -> resourceLiveData.error(resource.code!!, resource.message!!)
            }

        }
    }

    override  fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.remove(callback)
    }

    protected fun notifyChange() {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, 0, null)
    }

    protected fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, fieldId, null)
    }



}