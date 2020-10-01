package com.example.videorecordingapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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