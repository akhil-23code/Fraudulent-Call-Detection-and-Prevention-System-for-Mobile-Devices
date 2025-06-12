package com.ibrahim.scamdetect.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ibrahim.scamdetect.databinding.FragmentAudioScanBinding
import com.ibrahim.scamdetect.model.AnalysisResult
import com.ibrahim.scamdetect.ml.MLService // Assuming you have this
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class AudioScanActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAudioScanBinding
    private var selectedAudioUri: Uri? = null
    private lateinit var httpClient: OkHttpClient
    private lateinit var etTranscriptionResult: EditText // New EditText reference
    private lateinit var btnCheckScam: Button // New Button reference

    companion object {
        private const val STORAGE_PERMISSION_REQUEST = 1001
        private const val AUDIO_PICK_REQUEST = 1002
        private const val LOCAL_SERVER_URL = "http://192.168.158.101:5001/transcribe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAudioScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etTranscriptionResult = binding.etTranscriptionResult
        btnCheckScam = binding.btnCheckScam

        setupHttpClient()
        setupClickListeners()

        // Initially disable the Check for Scam button
        btnCheckScam.isEnabled = false
    }

    private fun setupHttpClient() {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun setupClickListeners() {
        binding.btnPickAudio.setOnClickListener {
            checkPermissionAndPickAudio()
        }

        binding.btnScanAudio.setOnClickListener {
            selectedAudioUri?.let { uri ->
                startAudioTranscription(uri)
            } ?: run {
                Toast.makeText(this, "Please select an audio file first", Toast.LENGTH_SHORT).show()
            }
        }

        btnCheckScam.setOnClickListener {
            val transcribedText = etTranscriptionResult.text.toString().trim()
            if (transcribedText.isNotEmpty()) {
                // Simulate ML service analysis (replace with actual MLService call)
                val result = MLService.analyzeText(transcribedText)
                Intent(this, ReportActivity::class.java).apply {
                    putExtra("EXTRA_TYPE", result.type)
                    putExtra("EXTRA_SCORE", result.score)
                    putExtra("EXTRA_DETAILS", result.details)
                }.also { startActivity(it) }
            } else {
                Toast.makeText(this, "No text to check for scam", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionAndPickAudio() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), STORAGE_PERMISSION_REQUEST)
        } else {
            openAudioPicker()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAudioPicker()
            } else {
                Toast.makeText(this, "Permission denied. Cannot access audio files.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openAudioPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }
        startActivityForResult(intent, AUDIO_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUDIO_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedAudioUri = uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to get persistent read permission for audio file.", Toast.LENGTH_LONG).show()
                    }
                }
                val fileName = uri.lastPathSegment ?: "Audio file selected"
                binding.tvAudioPath.text = fileName
                binding.btnScanAudio.isEnabled = true
                Toast.makeText(this, "Audio file selected: $fileName", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAudioTranscription(audioUri: Uri) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                clearPreviousResult()
                val audioFile = createTempFileFromUri(audioUri)
                if (audioFile == null) {
                    showError("Failed to prepare audio file for upload.")
                    return@launch
                }
                sendAudioToServer(audioFile) { transcriptionResult ->
                    runOnUiThread {
                        if (transcriptionResult != null) {
                            etTranscriptionResult.setText(transcriptionResult)
                            btnCheckScam.isEnabled = true // Enable check scam button after transcription
                        } else {
                            etTranscriptionResult.hint = "Transcription failed."
                            btnCheckScam.isEnabled = false
                        }
                        showLoading(false)
                        audioFile.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error during transcription process: ${e.localizedMessage}")
                showLoading(false)
            }
        }
    }

    private fun sendAudioToServer(audioFile: File, callback: (String?) -> Unit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "audio",
                audioFile.name,
                audioFile.asRequestBody("audio/*".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url(LOCAL_SERVER_URL)
            .post(requestBody)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null) // Indicate failure
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val transcription = jsonResponse.getString("transcription")
                        callback(transcription)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(null) // Indicate failure due to parsing
                    }
                } else {
                    callback(null) // Indicate failure due to server error
                }
            }
        })
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val tempFile = File.createTempFile("audio_", ".tmp", cacheDir)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                tempFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnScanAudio.isEnabled = !show
        binding.btnPickAudio.isEnabled = !show
    }

    private fun clearPreviousResult() {
        etTranscriptionResult.text.clear()
        etTranscriptionResult.hint = "Transcription will appear here"
        btnCheckScam.isEnabled = false
    }

    private fun showError(message: String) {
        etTranscriptionResult.hint = "Error: $message"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        btnCheckScam.isEnabled = false
    }
}