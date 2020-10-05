package com.example.videorecordingapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.videorecordingapp.R
import com.example.videorecordingapp.models.MyVideoCamera
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoRecordingActivity:AppCompatActivity() {

    private lateinit var videoCamera:MyVideoCamera

    private lateinit var videoPreviewView:PreviewView

    private lateinit var tvCountdownTimer:TextView

    private lateinit var videoCaptureUseCase: VideoCapture

    private lateinit var cameraExecutor:ExecutorService

    private var recordingDurationRemaining = 0

    private var recordingTitle = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_recording_preview)

        videoPreviewView = findViewById(R.id.video_recording_preview)

        tvCountdownTimer = findViewById(R.id.video_recording_countdown_timer)

        if (savedInstanceState == null){
            recordingDurationRemaining = intent.getIntExtra("RECORDING_DURATION",15)
            recordingTitle = intent.getStringExtra("RECORDING_TITLE")?:""
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onStart() {
        super.onStart()
        if (allPermissionsGranted()){
            startVideoRecording(VideoCapture.OutputFileOptions.Builder(getOutputFile()).build())
        }
        else{
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing){
            cameraExecutor.shutdown()
        }
    }

    override fun onBackPressed() {
        endRecording()
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startVideoRecording(VideoCapture.OutputFileOptions.Builder(getOutputFile()).build())
            } else {
                Toast.makeText(this,
                    "Permissions not granted. Cannot record video",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initCamera(){
        videoCamera = MyVideoCamera(this)
        val previewUseCase = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(videoPreviewView.surfaceProvider)
            }
        videoCaptureUseCase = VideoCapture.Builder().apply{
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
        }.build()
        videoCamera.bindToCamera(previewUseCase, videoCaptureUseCase)
    }

    @SuppressLint("RestrictedApi")
    private fun startVideoRecording(outputFileOptions: VideoCapture.OutputFileOptions){
        initCamera()
        videoCamera.isCameraBound.observe(this, {isBound ->
            if (isBound){
                videoCaptureUseCase.startRecording(outputFileOptions, cameraExecutor, object : VideoCapture.OnVideoSavedCallback{
                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        runOnUiThread{
                            Toast.makeText(this@VideoRecordingActivity,
                                "$recordingTitle saved at ${outputFileResults.savedUri}",
                                Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.e("VideoServiceActivity", message)
                    }

                })

                handleOrientationChange()

                val timer = object: CountDownTimer(recordingDurationRemaining * 1000L,
                    1 * 1000){
                    override fun onTick(remaingTimeInMilliSeconds: Long) {
                        val remainingTimeInSecs = remaingTimeInMilliSeconds / 1000
                        recordingDurationRemaining = remainingTimeInSecs.toInt()
                        val remainingTime = "${recordingDurationRemaining / 60}:" +
                                String.format("%02d", (recordingDurationRemaining % 60))
                        tvCountdownTimer.text = remainingTime
                    }

                    override fun onFinish() {
                        endRecording()
                    }

                }
                timer.start()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun endRecording() {
        videoCaptureUseCase.stopRecording()
        finish()
    }

    private fun handleOrientationChange(){
        val orientationEventListener = object : OrientationEventListener(this,SensorManager.SENSOR_DELAY_NORMAL){
            @SuppressLint("RestrictedApi")
            override fun onOrientationChanged(newOrientation: Int) {
                val rotation = when (newOrientation) {
                    in 45..134 -> {
                        Surface.ROTATION_270
                    }
                    in 135..224 -> {
                        Surface.ROTATION_180
                    }
                    in 225..314 -> {
                        Surface.ROTATION_90
                    }
                    else -> {
                        Surface.ROTATION_0
                    }
                }
                videoCaptureUseCase.setTargetRotation(rotation)

            }

        }
        orientationEventListener.enable()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun getOutputFile() = File(getOutputDirectory(),
        recordingTitle + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + ".mp4")

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}