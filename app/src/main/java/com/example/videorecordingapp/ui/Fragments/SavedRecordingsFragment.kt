package com.example.videorecordingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapp.R
import com.example.videorecordingapp.ui.Adapters.SavedRecordingsAdapter
import com.example.videorecordingapp.ui.ViewModels.MainActivityViewModel

class SavedRecordingsFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.tab_saved_recordings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        val recyclerView = view.findViewById<RecyclerView>(R.id.saved_recordings_grid)
        recyclerView.layoutManager = GridLayoutManager(view.context,2)
        vm.savedRecordings.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = SavedRecordingsAdapter(it)
        })
    }


}