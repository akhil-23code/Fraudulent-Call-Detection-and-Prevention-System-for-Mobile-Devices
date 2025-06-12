package com.ibrahim.scamdetect.ui

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.R
import com.ibrahim.scamdetect.databinding.FragmentAwarenessBinding

class AwarenessActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAwarenessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAwarenessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up VideoView with media controls
        val mc = MediaController(this)
        mc.setAnchorView(binding.videoSafety)

        // Build URI to raw resource
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.safety_tips}")
        binding.videoSafety.apply {
            setMediaController(mc)
            setVideoURI(videoUri)
            requestFocus()
            // Start paused—user taps Play
        }
    }
}
