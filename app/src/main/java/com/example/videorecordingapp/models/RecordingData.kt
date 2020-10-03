package com.example.videorecordingapp.models

import java.sql.Timestamp

const val MIN_RECORDING_TIME = 15


data class RecordingData(val name:String = "", val duration:Int = MIN_RECORDING_TIME, val timestamp:Timestamp? = null)