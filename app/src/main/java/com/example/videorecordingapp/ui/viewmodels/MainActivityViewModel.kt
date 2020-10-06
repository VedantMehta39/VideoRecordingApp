package com.example.videorecordingapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapp.models.RecordingData


class MainActivityViewModel: ViewModel() {
    var savedRecordings = MutableLiveData<ArrayList<RecordingData>>()

    fun addRecording(data:RecordingData){
        var oldSavedRecordings = savedRecordings.value
        if (oldSavedRecordings == null){
            oldSavedRecordings = ArrayList()

        }
        oldSavedRecordings.add(data)
        savedRecordings.value = oldSavedRecordings
    }
}