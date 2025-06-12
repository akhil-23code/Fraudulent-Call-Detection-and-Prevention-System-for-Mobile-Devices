package com.ibrahim.scamdetect.model

data class AnalysisResult(
    val type: String,      // e.g. "Audio" / "Text" / "Email"
    val score: Float,      // model output confidence
    val details: String    // human‑readable explanation
)
