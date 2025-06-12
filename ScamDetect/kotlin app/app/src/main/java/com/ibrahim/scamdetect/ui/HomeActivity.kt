package com.ibrahim.scamdetect.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.databinding.ActivityHomeBinding
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import android.view.MotionEvent
import android.view.View
import com.ibrahim.scamdetect.R


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // VideoView setup
        val videoView = binding.root.findViewById<VideoView>(R.id.videoSafety)

        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.safety_tips}")
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()

//        videoView.setOnPreparedListener {
//            videoView.start() // Optional: autoplay
//        }

        videoView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        binding.btnMakeCall.setOnClickListener {
            startActivity(Intent(this, MakeCallActivity::class.java))
        }
        binding.btnAudioScan.setOnClickListener {
            startActivity(Intent(this, AudioScanActivity::class.java))
        }
        binding.btnTextScan.setOnClickListener {
            startActivity(Intent(this, TextScanActivity::class.java))
        }
        binding.btnEmailScan.setOnClickListener {
            startActivity(Intent(this, EmailScanActivity::class.java))
        }
//        binding.btnAwareness.setOnClickListener {
//            startActivity(Intent(this, AwarenessActivity::class.java))
//        }
    }
}
