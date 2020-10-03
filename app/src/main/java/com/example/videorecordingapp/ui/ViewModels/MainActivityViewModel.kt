package com.example.videorecordingapp.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapp.models.RecordingData
import java.sql.Timestamp


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