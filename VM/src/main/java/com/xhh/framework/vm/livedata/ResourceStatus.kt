package com.xhh.framework.vm.livedata

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */
sealed class ResourceStatus<T>(val status:Int,val code:String?,val data:T?,val message:String?) {
    companion object{
        internal const val LOADING:Int = 1
        internal const val SUCCESS:Int  =2
        internal const val ERROR:Int= 3



    }

    internal class Loading<T>:ResourceStatus<T>(LOADING,null,null,null)
    internal class Success<T>(data: T?):ResourceStatus<T>(SUCCESS,null,data,null)
    internal class Error<T>(code: String?,message: String?):ResourceStatus<T>(ERROR,code,null,message)
}