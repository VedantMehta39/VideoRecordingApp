package com.example.videorecordingapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.OrientationEventListener
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
import com.example.videorecordingapp.models.RecordingData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VideoRecordingActivity:AppCompatActivity() {

    private lateinit var _videoCamera:MyVideoCamera

    private lateinit var _videoCaptureUseCase: VideoCapture

    private lateinit var _videoPreviewView:PreviewView

    private lateinit var _tvCountdownTimer:TextView

    private var _cameraExecutor:ExecutorService = Executors.newSingleThreadExecutor()

    private var _recordingDurationRemaining = 0

    private var _recordingTitle = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_recording_preview)

        _videoPreviewView = findViewById(R.id.video_recording_preview)

        _tvCountdownTimer = findViewById(R.id.video_recording_countdown_timer)

        if (savedInstanceState == null){
            _recordingDurationRemaining = intent.getIntExtra("RECORDING_DURATION",
                RecordingData.MIN_RECORDING_TIME)
            _recordingTitle = intent.getStringExtra("RECORDING_TITLE")?:""
        }
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
            _cameraExecutor.shutdown()
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
        _videoCamera = MyVideoCamera(this)
        // Set up the preview use case
        val previewUseCase = Preview.Builder()
            .build()
            .also {
                // Bind the preview to the SurfaceView on PreviewView
                it.setSurfaceProvider(_videoPreviewView.surfaceProvider)
            }
        // Set up the video capture use case
        _videoCaptureUseCase = VideoCapture.Builder().apply{
            // Set Aspect Ratio on the recorded video thats being stored
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
        }.build()

        // Bind Use Cases to the camera and the camera to the activity's lifecycle
        _videoCamera.bindToCamera(previewUseCase, _videoCaptureUseCase)
    }

    @SuppressLint("RestrictedApi")
    private fun startVideoRecording(outputFileOptions: VideoCapture.OutputFileOptions){
        initCamera()
        _videoCamera.isCameraBound.observe(this, { isBound ->
            if (isBound){
                _videoCaptureUseCase.startRecording(outputFileOptions, _cameraExecutor, object : VideoCapture.OnVideoSavedCallback{
                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        runOnUiThread{
                            Toast.makeText(this@VideoRecordingActivity,
                                "$_recordingTitle saved at ${outputFileResults.savedUri}",
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
                updateUIOnOrientationChange()
                startCountDown()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun endRecording() {
        if(::_videoCaptureUseCase.isInitialized){
            _videoCaptureUseCase.stopRecording()
        }
        finish()
    }

    private fun startCountDown(){
        val timer = object: CountDownTimer(_recordingDurationRemaining * 1000L,
            1 * 1000){
            override fun onTick(remaingTimeInMilliSeconds: Long) {
                val remainingTimeInSecs = remaingTimeInMilliSeconds / 1000
                _recordingDurationRemaining = remainingTimeInSecs.toInt()
                val remainingTime = "${_recordingDurationRemaining / 60}:" +
                        String.format("%02d", (_recordingDurationRemaining % 60))
                _tvCountdownTimer.text = remainingTime
            }

            override fun onFinish() {
                endRecording()
            }

        }
        timer.start()
    }

    private fun updateUIOnOrientationChange(){
        val orientationEventListener = object : OrientationEventListener(this,SensorManager.SENSOR_DELAY_NORMAL){
            @SuppressLint("RestrictedApi")
            override fun onOrientationChanged(newOrientation: Int) {
                _tvCountdownTimer.rotation = when (newOrientation) {
                    in 0..89 -> {
                        0f
                    }
                    in 90..179 -> {
                        -90f
                    }
                    in 270..360 -> {
                        -270f
                    }
                    else ->{
                        _tvCountdownTimer.rotation
                    }
                }
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
        _recordingTitle + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
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