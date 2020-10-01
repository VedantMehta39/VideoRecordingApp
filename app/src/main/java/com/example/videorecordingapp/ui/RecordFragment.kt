package com.example.videorecordingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.videorecordingapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText

class RecordFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.tab_record_fragment,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etRecordingName = view.findViewById<TextInputEditText>(R.id.recording_name)
        val tvMinutesSelected = view.findViewById<TextView>(R.id.recording_time_in_min)
        val tvSecondsSelected = view.findViewById<TextView>(R.id.recording_time_in_sec)
        val sliderRecordingTime = view.findViewById<Slider>(R.id.recording_time_slider)
        val btnRecord = view.findViewById<MaterialButton>(R.id.btn_record)

        sliderRecordingTime.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val totalSeconds = slider.value
                tvMinutesSelected.text = (totalSeconds/60).toInt().toString()
                tvSecondsSelected.text = (totalSeconds%60).toInt().toString()
            }
        })

        btnRecord.setOnClickListener {
            val recordingName = etRecordingName.text.toString()
            if (recordingName.isEmpty()){
                etRecordingName.error = ""
            }
            else{
                etRecordingName.error = null
                Toast.makeText(requireContext(),"$recordingName will be recorded for " +
                        "${tvMinutesSelected.text} mins and ${tvSecondsSelected.text} seconds",
                    Toast.LENGTH_LONG).show()
            }
        }

    }
}