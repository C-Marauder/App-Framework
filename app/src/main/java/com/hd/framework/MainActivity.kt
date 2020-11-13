package com.hd.framework

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hd.framework.model.City
import com.hd.framework.model.House
import com.xhh.framework.vm.AppViewModel
import com.xhh.framework.vm.http.Resource
import com.xhh.framework.vm.utils.register

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

