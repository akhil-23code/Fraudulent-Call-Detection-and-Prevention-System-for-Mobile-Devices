package com.ibrahim.scamdetect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.databinding.ActivityEmailScanBinding
import com.ibrahim.scamdetect.ml.MLService
import com.ibrahim.scamdetect.model.AnalysisResult

class EmailScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanEmail.setOnClickListener {
            val emailBody = binding.etEmailInput.text.toString().trim()
            if (emailBody.isEmpty()) {
                Toast.makeText(this, "Please enter email body", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Reuse the same analyzeText() for email content
            val result: AnalysisResult = MLService.analyzeText(emailBody)
                .copy(type = "Email")  // adjust the type label

            // Launch the report screen
            Intent(this, ReportActivity::class.java).apply {
                putExtra("EXTRA_TYPE",    result.type)
                putExtra("EXTRA_SCORE",   result.score)
                putExtra("EXTRA_DETAILS", result.details)
            }.also { startActivity(it) }
        }
    }
}
