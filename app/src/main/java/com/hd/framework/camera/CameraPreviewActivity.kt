package com.hd.framework.camera

import android.Manifest
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.hd.framework.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *   @Author:小灰灰
 *   @Time:2020/11/6
 *   @Desc:
 */
class CameraPreviewActivity : AppCompatActivity() {
    private lateinit var cameraViewModel: CameraViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(
            application, FirebaseOptions.Builder()
                .setApplicationId(packageName)
                .build(), "[DEFAULT]"
        )

        setContentView(R.layout.activity_camera)
        cameraViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(
            CameraViewModel::class.java
        )
        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startCamera()

            }
        }
        launcher.launch(Manifest.permission.CAMERA)
    }

    private val cameraExcutor: Executor by lazy {
        Executors.newSingleThreadExecutor()
    }
    private val faceDetector: FaceDetector by lazy {
        val faceDetectorOptions = FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()
        FaceDetection.getClient(faceDetectorOptions)
    }

    private fun startCamera() {
        cameraViewModel.getProcessCameraProvider().observe(this) {
            it.unbindAll()
            val previewUseCase = Preview.Builder()
                .build()
                .also { pv ->
                    pv.setSurfaceProvider(preview.surfaceProvider)
                }
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExcutor) { imageProxy ->

                        imageProxy.image?.let { image ->
                            faceDetector.process(InputImage.fromMediaImage(image,imageProxy.imageInfo.rotationDegrees))
                                .addOnSuccessListener {
                                    it.forEach {face->
                                        Log.e("==","$face")
                                    }
                                }

                        }
                        imageProxy.close()
                    }
                }
            it.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_FRONT_CAMERA,
                previewUseCase,
                imageAnalysis
            )
        }
    }
    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    override fun onPause() {
        super.onPause()
    }
}

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var cameraProviderLiveData: MutableLiveData<ProcessCameraProvider>

    fun getProcessCameraProvider(): MutableLiveData<ProcessCameraProvider> {
        if (!::cameraProviderLiveData.isInitialized) {
            cameraProviderLiveData = MutableLiveData()
        }
        val listenableFuture = ProcessCameraProvider.getInstance(getApplication())
        listenableFuture.addListener({
            try {
                cameraProviderLiveData.value = listenableFuture.get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(getApplication()))
        return cameraProviderLiveData
    }
}

