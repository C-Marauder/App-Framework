package com.xhh.ui.result

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 *   @Author:小灰灰
 *   @Time:2020/11/1
 *   @Desc:
 */
class ResultCaller private constructor(private val activityResultCaller: ActivityResultCaller) {

    companion object {
        fun create(activityResultCaller: ActivityResultCaller) = ResultCaller(activityResultCaller)
        const val CAMERA_BITMAP: Int = 1
        const val CAMERA_URI: Int = 2
        const val GALLERY: Int = 3
        const val PERMISSION: Int = 4
        const val PERMISSION_MULTI: Int = 5
    }

    private var mCameraLauncher: ActivityResultLauncher<Void>? = null
    private var mPictureUriLauncher: ActivityResultLauncher<Void>? = null
    private var mGalleryLauncher: ActivityResultLauncher<Void>? = null
    private var mPermissionLauncher: ActivityResultLauncher<String>? = null
    private var mPermissionsLauncher: ActivityResultLauncher<Array<out String>>? = null

    @Suppress("UNCHECKED_CAST")
    fun <T> register(resultType: Int, onResultCallback: (result: T) -> Unit) {
        val activityResultContracts = when (resultType) {
            CAMERA_BITMAP -> ActivityResultContracts.TakePicturePreview()
            CAMERA_URI -> TakePictureForBitmap()
            GALLERY -> PickPictureFromGallery()
            PERMISSION -> ActivityResultContracts.RequestPermission()
            PERMISSION_MULTI -> ActivityResultContracts.RequestMultiplePermissions()
            else -> throw Exception("unSupport resultType ")
        }
        val launcher = activityResultCaller.registerForActivityResult(activityResultContracts) {
            onResultCallback(it as T)
        }
        when (resultType) {
            CAMERA_BITMAP -> mCameraLauncher = launcher as ActivityResultLauncher<Void>
            CAMERA_URI -> mPictureUriLauncher = launcher as ActivityResultLauncher<Void>
            GALLERY -> mGalleryLauncher = launcher as ActivityResultLauncher<Void>
            PERMISSION -> mPermissionLauncher = launcher as ActivityResultLauncher<String>
            PERMISSION_MULTI -> mPermissionsLauncher =
                launcher as ActivityResultLauncher<Array<out String>>
        }


    }

    fun request(vararg permission: String) {
        if (permission.isNullOrEmpty()) {
            if (permission.size == 1) {
                mPermissionLauncher?.launch(permission[0])
            } else {
                mPermissionsLauncher?.launch(permission)
            }
        }
    }

    fun start(resultType: Int) {
        when (resultType) {
            CAMERA_BITMAP -> mCameraLauncher?.launch(null)
            CAMERA_URI -> mPictureUriLauncher?.launch(null)

            GALLERY -> mGalleryLauncher?.launch(null)
        }
    }
}