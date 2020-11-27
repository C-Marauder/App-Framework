package com.xhh.framework.vm.utils


import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LifecycleCoroutineScope
import com.xhh.framework.vm.helper.AppHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 *   @Author:小灰灰
 *   @Time:2020/11/18
 *   @Desc:
 */

inline fun <reified T : LifecycleCoroutineScope> T.saveConfig(vararg params: Pair<String, Any>) {
    if (params.isNotEmpty()) {
        launch {
            params.forEach {pair->
                AppHelper.dataStore.edit {
                    it[preferencesKey(pair.first)] = pair.second
                }
            }
        }
    }


}


 inline fun <reified T:LifecycleCoroutineScope,reified E> T.readConfig(key:String,noinline onResult:(value:E?)->Unit){
    launch {
        val value = AppHelper.dataStore.data.map {
            it[preferencesKey(key)]
        }.first()
        val realValue = if (value!=null){
            value as E
        }else{
            null
        }
        onResult(realValue)

    }

}