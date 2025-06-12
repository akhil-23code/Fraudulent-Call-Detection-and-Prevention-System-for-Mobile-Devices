package com.ibrahim.scamdetect.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.GridLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibrahim.scamdetect.R

class MakeCallActivity : AppCompatActivity() {

    private lateinit var tvNumberDisplay: TextView
    private lateinit var btnCall: FloatingActionButton
    private val CALL_PERMISSION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_call)

        tvNumberDisplay = findViewById(R.id.tvNumberDisplay)
        btnCall = findViewById(R.id.btnCall)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        // Dial pad input logic
        val gridDialPad = findViewById<GridLayout>(R.id.gridDialPad)
        for (i in 0 until (gridDialPad?.childCount ?: 0)) {
            val view = gridDialPad?.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    val digit = view.text.toString()
                    tvNumberDisplay.append(digit)
                }
            }
        }

        // Call button logic
        btnCall.setOnClickListener {
            val number = tvNumberDisplay.text.toString()
            if (number.isNotEmpty()) {
                makePhoneCall(number)
            } else {
                Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete button logic
        btnDelete.setOnClickListener {
            val currentText = tvNumberDisplay.text.toString()
            if (currentText.isNotEmpty()) {
                tvNumberDisplay.text = currentText.substring(0, currentText.length - 1)
            }
        }
    }

    private fun makePhoneCall(number: String) {
        val intent = Intent(Intent.ACTION_DIAL) // Use ACTION_DIAL to open default dialer
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }
}