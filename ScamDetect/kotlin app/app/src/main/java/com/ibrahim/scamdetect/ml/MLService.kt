package com.ibrahim.scamdetect.ml

import com.ibrahim.scamdetect.App
import com.ibrahim.scamdetect.model.AnalysisResult
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

object MLService {
    private val interpreter: Interpreter = App.tfLite

    fun analyzeText(text: String): AnalysisResult {
        val vector = Vectorizer.transform(text)
        val inputBuf = ByteBuffer.allocateDirect(4 * vector.size)
            .order(ByteOrder.nativeOrder())
        vector.forEach { inputBuf.putFloat(it) }
        inputBuf.rewind()

        val outputBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
        interpreter.run(inputBuf, outputBuf)
        outputBuf.rewind()
        val score = outputBuf.float
        val label = if (score > 0.5f) "Scam" else "Safe"
        return AnalysisResult("Text", score, label)
    }

    /**
     * Stub for audio analysis.
     * For now, returns “Not supported” with 0.0 score.
     * Later you can replace with your own audio TFLite model.
     */
    fun analyzeAudio(context: android.content.Context, audioBuffer: FloatArray): AnalysisResult {
        // TODO: replace with real audio model inference
        return AnalysisResult(
            type    = "Audio",
            score   = 0.0f,
            details = "Audio scanning not yet implemented"
        )
    }
}



//// File: MLService.kt (com.ibrahim.scamdetect.ml)
//package com.ibrahim.scamdetect.ml
//
//import com.ibrahim.scamdetect.App
//import com.ibrahim.scamdetect.model.AnalysisResult
//import org.tensorflow.lite.Interpreter
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//object MLService {
//    private val interpreter: Interpreter = App.tfLite
//
//    fun analyzeText(text: String): AnalysisResult {
//        val vector = Vectorizer.transform(text)
//        val inputBuf = ByteBuffer.allocateDirect(4 * vector.size)
//            .order(ByteOrder.nativeOrder())
//        vector.forEach { inputBuf.putFloat(it) }
//        inputBuf.rewind()
//
//        val outputBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
//        interpreter.run(inputBuf, outputBuf)
//        outputBuf.rewind()
//        val score = outputBuf.float
//
//        val label = if (score > 0.5f) "Scam" else "Safe"
//        return AnalysisResult(type = "Text", score = score, details = label)
//    }
//
//    fun analyzeAudio(context: android.content.Context, audioBuffer: FloatArray): AnalysisResult {
//        // TODO: replace with real audio model inference
//        return AnalysisResult(
//            type    = "Audio",
//            score   = 0.0f,
//            details = "Audio scanning not yet implemented"
//        )
//    }
//}
