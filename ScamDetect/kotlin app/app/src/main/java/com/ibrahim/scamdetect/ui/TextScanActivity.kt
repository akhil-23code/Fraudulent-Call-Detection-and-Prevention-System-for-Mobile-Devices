package com.ibrahim.scamdetect.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.databinding.ActivityTextScanBinding
import com.ibrahim.scamdetect.model.AnalysisResult
import com.ibrahim.scamdetect.ml.MLService

class TextScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanText.setOnClickListener {
            val inputText = binding.etTextInput.text.toString().trim()
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Run inference using our TFLite-backed service
            val result: AnalysisResult = MLService.analyzeText(inputText)

            // Launch the report screen with the model’s output
            Intent(this, ReportActivity::class.java).apply {
                putExtra("EXTRA_TYPE",   result.type)
                putExtra("EXTRA_SCORE",  result.score)
                putExtra("EXTRA_DETAILS", result.details)
            }.also { startActivity(it) }
        }
    }
}


//package com.ibrahim.scamdetect.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.ibrahim.scamdetect.databinding.ActivityTextScanBinding
//import com.ibrahim.scamdetect.ml.MLService
//
//class TextScanActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityTextScanBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityTextScanBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnScanText.setOnClickListener {
//            val text = binding.etTextInput.text.toString().trim()
//            if (text.isEmpty()) {
//                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
//            } else {
//                val result = MLService.analyzeText(this, text)
//                Intent(this, ReportActivity::class.java).apply {
//                    putExtra("EXTRA_TYPE", result.type)
//                    putExtra("EXTRA_SCORE", result.score)
//                    putExtra("EXTRA_DETAILS", result.details)
//                    startActivity(this)
//                }
//            }
//        }
//    }
//}
