// File: App.kt (com.ibrahim.scamdetect)
package com.ibrahim.scamdetect

import android.app.Application
import com.ibrahim.scamdetect.ml.Vectorizer
import org.tensorflow.lite.Interpreter
import java.nio.channels.FileChannel

class App : Application() {
    companion object {
        lateinit var tfLite: Interpreter
    }

    override fun onCreate() {
        super.onCreate()
        // Load TFLite
        assets.openFd("model.tflite").use { fd ->
            val stream = fd.createInputStream()
            val map = stream.channel.map(
                FileChannel.MapMode.READ_ONLY,
                fd.startOffset,
                fd.declaredLength
            )
            tfLite = Interpreter(map)
        }
        // Initialize Vectorizer
        Vectorizer.initFromAsset(this, "vectorizer.json")
    }
}
