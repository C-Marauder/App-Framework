package com.xhh.framework.vm.network

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData

/**
 *   @Author:小灰灰
 *   @Time:2020/10/24
 *   @Desc:网络状态监听
 */
internal class AppNetworkObserver constructor(private val application: Application){

    private val mNetworkLiveData:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val mConnectivityManager:ConnectivityManager by lazy {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            application.getSystemService(ConnectivityManager::class.java)
        } else {
            application.getSystemService(Context.CONNECTIVITY_SERVICE)
        } as ConnectivityManager
    }
    private val mWifiManager:WifiManager by lazy {
        application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val mTelephonyManager:TelephonyManager by lazy {
        application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }
    internal val networkType:Int get() {
        return if (mWifiManager.isWifiEnabled && mWifiManager.wifiState == WifiManager.WIFI_STATE_ENABLED){
            NetworkStatus.WIFI
        }else{
            if (mTelephonyManager.dataState == TelephonyManager.DATA_CONNECTED){
                NetworkStatus.PHONE
            }else{
                NetworkStatus.NONE
            }
        }
    }

    internal val isOnline:Boolean get() {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mConnectivityManager.getNetworkCapabilities(mConnectivityManager.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }else{
            mConnectivityManager.activeNetworkInfo?.isConnected
        } == true
    }
    init {

        application.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is AppCompatActivity){
                    if (activity is OnNetworkStatusChangeListener){
                        mNetworkLiveData.observe(activity){
                            activity.onChanged(isOnline,it)
                        }
                    }
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                        FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            super.onFragmentCreated(fm, f, savedInstanceState)
                            if (f is OnNetworkStatusChangeListener){
                                mNetworkLiveData.observe(f){
                                    f.onChanged(isOnline,it)
                                }
                            }

                        }

                        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                            super.onFragmentDestroyed(fm, f)
                            if (f is OnNetworkStatusChangeListener){
                                mNetworkLiveData.removeObservers(f)
                            }
                        }

                    },true)
                }



            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity is OnNetworkStatusChangeListener && activity is AppCompatActivity){
                    mNetworkLiveData.removeObservers(activity)
                }

            }

        })
        observerNetwork()
    }

    private fun observerNetwork(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mConnectivityManager.registerDefaultNetworkCallback(object :ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    mNetworkLiveData.postValue(NetworkStatus.CONNECTED)

                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        mNetworkLiveData.postValue(NetworkStatus.WIFI)
                    }else {
                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                            mNetworkLiveData.postValue(NetworkStatus.PHONE)
                        }
                    }


                }
                override fun onLost(network: Network) {
                    super.onLost(network)
                    mNetworkLiveData.postValue(NetworkStatus.LOST)

                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    mNetworkLiveData.postValue(NetworkStatus.NONE)
                }
            })
        }else{
            application.registerReceiver(NetworkReceiver(), IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY))
        }
    }

    private inner class NetworkReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == ConnectivityManager.CONNECTIVITY_ACTION){
                    val networkInfo = it.getParcelableExtra(ConnectivityManager.EXTRA_EXTRA_INFO) as NetworkInfo
                    //networkInfo.type
                    mNetworkLiveData.value = when(networkInfo.state){
                        NetworkInfo.State.CONNECTED-> when(networkInfo.type){
                            ConnectivityManager.TYPE_WIFI-> NetworkStatus.WIFI
                            else-> NetworkStatus.PHONE
                        }
                        NetworkInfo.State.DISCONNECTED->NetworkStatus.LOST
                        NetworkInfo.State.UNKNOWN->NetworkStatus.NONE
                        else -> NetworkStatus.NONE
                    }
                }
            }
        }


    }

}