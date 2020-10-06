package com.example.videorecordingapp.ui

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.videorecordingapp.ui.fragments.RecordFragment
import com.example.videorecordingapp.ui.fragments.SavedRecordingsFragment

class TabAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity){
    override fun getItemCount() = 2

    override fun createFragment(position: Int) =
        when(position){
            0 -> {
                RecordFragment()
            }
            1 -> {
                SavedRecordingsFragment()
            }
            else -> RecordFragment()
        }


}