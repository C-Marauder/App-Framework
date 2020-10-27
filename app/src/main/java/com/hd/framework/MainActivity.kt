package com.hd.framework

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hd.framework.model.City
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.framework.vm.utils.register

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("===","==》》》")
        val myViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MyViewModel::class.java)
        myViewModel.register<City>(Api.GET_CITY,this,onLoading = {

        },onSuccess = {

        },onError ={
            code, msg ->

        })
        myViewModel.load(Api.GET_CITY)
    }
}

class MyViewModel(application: Application) : AppViewModel(application) {
    override fun onDispatchService(service: String): Resource<*> {
       return App.api.getMyHouse()
    }

    override fun onCallRequest(service: String) {
        request<City>(service)
    }


}