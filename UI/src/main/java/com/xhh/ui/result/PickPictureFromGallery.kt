package com.xhh.ui.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import com.xhh.ui.App
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 *   @Author:小灰灰
 *   @Time:2020/11/1
 *   @Desc:
 */
internal class PickPictureFromGallery : ActivityResultContract<Void, Bitmap?>() {

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
       return if (resultCode == Activity.RESULT_OK){
           intent?.data?.let {
               BitmapFactory.decodeStream(App.instance.contentResolver.openInputStream(it))

           }
       }else{
           null
       }
    }

    override fun createIntent(context: Context, input: Void?): Intent {

        return Intent(Intent.ACTION_PICK,null).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        }
    }


}