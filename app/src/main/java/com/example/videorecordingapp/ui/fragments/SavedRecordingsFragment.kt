package com.example.videorecordingapp.ui.fragments

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
import com.example.videorecordingapp.ui.adapters.SavedRecordingsAdapter
import com.example.videorecordingapp.ui.viewmodels.MainActivityViewModel

class SavedRecordingsFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.tab_saved_recordings_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val vm = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        val recyclerView = view.findViewById<RecyclerView>(R.id.saved_recordings_grid)
        recyclerView.layoutManager = GridLayoutManager(view.context,2)
        vm.savedRecordings.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = SavedRecordingsAdapter(it)
        })
    }


}