package com.example.videorecordingapp

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.videorecordingapp.ui.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator

val TAB_TITLES = arrayOf("Record", "Saved Recordings")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = TabAdapter(this)
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager){ tab: TabLayout.Tab, i: Int ->
            tab.text = TAB_TITLES[i]
        }.attach()

    }
}