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
import com.example.videorecordingapp.ui.ViewModels.RecordTabViewModel
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
        val vm = ViewModelProvider(this).get(RecordTabViewModel::class.java)


        val etRecordingName = view.findViewById<TextInputEditText>(R.id.recording_name)
        val tvMinutesSelected = view.findViewById<TextView>(R.id.recording_time_in_min)
        val tvSecondsSelected = view.findViewById<TextView>(R.id.recording_time_in_sec)
        val sliderRecordingTime = view.findViewById<Slider>(R.id.recording_time_slider)
        val btnRecord = view.findViewById<MaterialButton>(R.id.btn_record)

        etRecordingName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                vm.recordingName = text.toString()
            }

        })

        vm.recordingTimeLimit.observe(viewLifecycleOwner, {seconds ->
            tvMinutesSelected.text = (seconds/60).toString()
            tvSecondsSelected.text = (seconds%60).toString()
        })


        sliderRecordingTime.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            vm.recordingTimeLimit.value = value.toInt()
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