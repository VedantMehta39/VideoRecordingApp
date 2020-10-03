package com.example.videorecordingapp.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapp.R
import com.example.videorecordingapp.models.RecordingData
import kotlin.collections.ArrayList

class SavedRecordingsAdapter(var data: ArrayList<RecordingData>):RecyclerView.Adapter<SavedRecordingsAdapter.RecordingDataViewHolder>(){
    class RecordingDataViewHolder(var view: View):RecyclerView.ViewHolder(view){

        fun bindData(data:RecordingData){
            val tvTitle = view.findViewById<TextView>(R.id.saved_recording_title)
            val tvDuration = view.findViewById<TextView>(R.id.saved_recording_duration)
            val tvTimestamp = view.findViewById<TextView>(R.id.saved_recording_timestamp)

            tvTitle.text = data.name
            val timeString = "${(data.duration / 60)}:${(data.duration % 60)}"
            tvDuration.text = timeString
            tvTimestamp.text = data.timestamp.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_recording,parent, false)
        return RecordingDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordingDataViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount() = data.size
}