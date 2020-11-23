package com.xhh.framework.vm

import android.app.Application
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


    fun updateUIByEvent(event: String) {
        mMessageObserver.value = event
    }
    open fun load(service: String){
        onCallRequest(service)
    }
    open fun <T> execute(service: String){
        onCallRequest(service)
    }
    open fun execute(vararg services: String){
        if (services.isNotEmpty()){
            viewModelScope.async(Dispatchers.IO) {
                try {
                    services.forEach {
                        async {
                            realExecute<Any>(it)
                        }
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    open fun networksSync(vararg services: String){
        if (!services.isNullOrEmpty()){
            viewModelScope.async(Dispatchers.IO) {
                repeat(services.size){
                    launch {
                        realExecute<Any>(services[it])
                    }
                }

            }
        }
    }
    /**
     * 执行单个任务
     */
    open fun network(service: String){
        onCallRequest(service)
    }
    /**
     * 执行多个并发任务
     */
    open fun networks(vararg services: String){
        if (!services.isNullOrEmpty()){

            viewModelScope.async(Dispatchers.IO){
                repeat(services.size){index->
                    async {
                        realExecute<Any>(services[index])
                    }
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    internal fun <T> getResourceLiveData(service: String): ResourceLiveData<T> {
        var resourceStatus = mResourceCallbacks[service]
        if (resourceStatus == null) {
            resourceStatus = ResourceLiveData<T>()
            mResourceCallbacks[service] = resourceStatus
        }
        return resourceStatus as ResourceLiveData<T>
    }

    protected abstract fun onDispatchService(service: String): Resource<*>
    protected abstract fun onCallRequest(service: String)
    abstract fun disConnected()

    protected fun <T>request(service: String){
        if (AppHelper.isOnline){
            viewModelScope.launch(Dispatchers.IO) {
                realExecute<T>(service)
            }
        } else {
            disConnected()
        }

    }

    protected fun <T>requestAsync(service: String){
        if (AppHelper.isOnline){
            viewModelScope.async(Dispatchers.IO) {
                realExecute<T>(service)
            }
        }else{
            disConnected()
        }

    }

    @Suppress("UNCHECKED_CAST")
    private  fun <T> realExecute(service: String) {
        val resourceLiveData = getResourceLiveData<T>(service)
        resourceLiveData.loading()
        when (val resource = onDispatchService(service)) {
            is Resource.Success -> resourceLiveData.success(resource.data as T)
            is Resource.Error -> resourceLiveData.error(resource.code!!, resource.message!!)
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