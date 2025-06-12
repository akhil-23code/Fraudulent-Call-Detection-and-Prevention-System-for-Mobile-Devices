package com.ibrahim.scamdetect.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Data Models
data class EmailRequest(val email: String)
data class OTPRequest(val email: String, val otp: String)
data class ResponseMessage(val message: String?, val error: String?)

// Retrofit Interface
interface AuthService {
    @POST("send_otp")
    fun sendOtp(@Body request: EmailRequest): Call<ResponseMessage>

    @POST("verify_otp")
    fun verifyOtp(@Body request: OTPRequest): Call<ResponseMessage>
}
