package com.example.videorecordingapp.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapp.models.RecordingData



class MainActivityViewModel: ViewModel() {
    var savedRecordings = MutableLiveData<ArrayList<RecordingData>>()
}