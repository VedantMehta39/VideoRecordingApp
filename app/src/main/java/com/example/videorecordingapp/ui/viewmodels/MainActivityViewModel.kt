package com.example.videorecordingapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapp.models.RecordingData


class MainActivityViewModel: ViewModel() {
    private var _savedRecordings = MutableLiveData<ArrayList<RecordingData>>()
    val savedRecordings:LiveData<ArrayList<RecordingData>>
    get() = _savedRecordings

    fun addRecording(data:RecordingData){
        var oldSavedRecordings = _savedRecordings.value
        if (oldSavedRecordings == null){
            oldSavedRecordings = ArrayList()

        }
        oldSavedRecordings.add(data)
        _savedRecordings.value = oldSavedRecordings
    }
}