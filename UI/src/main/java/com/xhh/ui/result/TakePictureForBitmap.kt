package com.xhh.ui.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
internal class TakePictureForBitmap: ActivityResultContract<Void, Bitmap?>() {

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)

    }
    private lateinit var mImageFile: File
    override fun createIntent(context: Context, input: Void?): Intent {
        val storageDir = App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val prefix = simpleDateFormat.format(Date())
        mImageFile = File.createTempFile(prefix, ".jpg", storageDir)
        val uri = FileProvider.getUriForFile(
            App.instance,
            App.instance.packageName + ".fileprovider",
            mImageFile)
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        return if (resultCode == Activity.RESULT_OK){
            BitmapFactory.decodeFile(mImageFile.absolutePath)
        }else{
            null
        }
    }


}