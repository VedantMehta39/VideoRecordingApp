package com.example.videorecordingapp.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val MIN_RECORDING_TIME = 15

class RecordTabViewModel: ViewModel() {
    var recordingName = ""
    var recordingTimeLimit = MutableLiveData(MIN_RECORDING_TIME)
}