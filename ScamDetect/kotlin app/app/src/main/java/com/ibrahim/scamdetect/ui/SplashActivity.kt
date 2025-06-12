package com.ibrahim.scamdetect.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.scamdetect.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Inflate the correct binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Post a delay on the logoImage view otp (ensures the view has attached)
        binding.logoImage.postDelayed({
            // Navigate to OTP screen first, not Home
            startActivity(Intent(this, OtpLoginActivity::class.java))
            finish()
        }, 2000L)


//        binding.logoImage.postDelayed({
//            // Navigate to HomeActivity after 2 seconds
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }, 2000L)
    }
}
