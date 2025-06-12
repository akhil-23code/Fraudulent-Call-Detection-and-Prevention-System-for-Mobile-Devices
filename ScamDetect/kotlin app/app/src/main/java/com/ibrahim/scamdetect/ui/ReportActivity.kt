package com.ibrahim.scamdetect.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("EXTRA_TYPE") ?: "Unknown"
        val score = intent.getFloatExtra("EXTRA_SCORE", 0f)
        val details = intent.getStringExtra("EXTRA_DETAILS") ?: ""

        binding.tvReportType.text = "Scan Type: $type"
        binding.tvReportScore.text = "Scam Score: ${"%.2f".format(score)}"
        binding.tvReportDetails.text = "Details:\n$details"
    }
}
