package com.example.videorecordingapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.videorecordingapp.R
import com.example.videorecordingapp.models.RecordingData
import com.example.videorecordingapp.ui.ViewModels.MainActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import java.sql.Timestamp

class RecordFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.tab_record_fragment,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        val etRecordingName = view.findViewById<TextInputEditText>(R.id.recording_name)
        val tvMinutesSelected = view.findViewById<TextView>(R.id.recording_time_in_min)
        val tvSecondsSelected = view.findViewById<TextView>(R.id.recording_time_in_sec)
        val sliderRecordingTime = view.findViewById<Slider>(R.id.recording_time_slider)
        val btnRecord = view.findViewById<MaterialButton>(R.id.btn_record)

        sliderRecordingTime.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            tvMinutesSelected.text = (value/60).toInt().toString()
            tvSecondsSelected.text = (value%60).toInt().toString()
        })

        btnRecord.setOnClickListener {
            val recordingName = etRecordingName.text.toString()
            if (recordingName.isEmpty()){
                etRecordingName.error = "Recording Name cannot be empty"
            }
            else{
                etRecordingName.error = null
                val newRecording = RecordingData(recordingName,
                    sliderRecordingTime.value.toInt(),
                    Timestamp(System.currentTimeMillis()))
                vm.addRecording(newRecording)
                Toast.makeText(requireContext(),"$recordingName will be recorded for " +
                        "${tvMinutesSelected.text} mins and ${tvSecondsSelected.text} seconds",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

}