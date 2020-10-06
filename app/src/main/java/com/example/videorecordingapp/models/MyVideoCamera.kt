package com.example.videorecordingapp.models

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.util.concurrent.ListenableFuture
import java.lang.Exception


class MyVideoCamera(var activity:AppCompatActivity) {

    private var _isCameraBound = MutableLiveData(false)
    val isCameraBound:LiveData<Boolean>
    get() = _isCameraBound

    private var _cameraProviderFuture = getCamera()

    private fun getCamera():ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(activity)

    @SuppressLint("RestrictedApi")
    fun bindToCamera(previewUseCase:Preview, videoCaptureUseCase:VideoCapture){
        _cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = _cameraProviderFuture.get()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, previewUseCase, videoCaptureUseCase)

            } catch(exc: Exception) {
                Log.e("VideoRecordingActivity", "Use case binding failed", exc)
            }
            _isCameraBound.value = true
        }, ContextCompat.getMainExecutor(activity))
    }

}