package com.example.videorecordingapp.models

import java.sql.Timestamp


data class RecordingData(val name:String = "", val duration:Int = MIN_RECORDING_TIME,
                         val timestamp:Timestamp? = null){
    companion object{
        const val MIN_RECORDING_TIME = 15
    }
}