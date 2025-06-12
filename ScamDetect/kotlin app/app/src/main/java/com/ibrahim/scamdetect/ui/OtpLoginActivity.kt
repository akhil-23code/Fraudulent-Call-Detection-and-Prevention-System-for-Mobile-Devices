package com.ibrahim.scamdetect.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.R
import com.ibrahim.scamdetect.network.EmailRequest
import com.ibrahim.scamdetect.network.OTPRequest
import com.ibrahim.scamdetect.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import com.ibrahim.scamdetect.network.ResponseMessage


class OtpLoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var otpEditText: EditText
    private lateinit var sendOtpButton: Button
    private lateinit var verifyOtpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_login)  // or R.layout.activity_main if using MainActivity

        emailEditText = findViewById(R.id.emailEditText)
        otpEditText = findViewById(R.id.otpEditText)
        sendOtpButton = findViewById(R.id.sendOtpButton)
        verifyOtpButton = findViewById(R.id.verifyOtpButton)

        sendOtpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = EmailRequest(email)
            RetrofitClient.instance.sendOtp(request).enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@OtpLoginActivity, "OTP sent to $email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@OtpLoginActivity, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    Toast.makeText(this@OtpLoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        verifyOtpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val otp = otpEditText.text.toString().trim()

            if (otp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = OTPRequest(email, otp)
            RetrofitClient.instance.verifyOtp(request).enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@OtpLoginActivity, "OTP Verified!", Toast.LENGTH_SHORT).show()
                        // Move to HomeActivity
                        startActivity(Intent(this@OtpLoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@OtpLoginActivity, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    Toast.makeText(this@OtpLoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
