package com.ibrahim.scamdetect.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object TFLiteModelLoader {
    private var interpreter: Interpreter? = null

    /** Returns a singleton Interpreter, loading the FlatBuffer from assets/model.tflite */
    fun getInterpreter(context: Context): Interpreter {
        return interpreter ?: Interpreter(loadModelFile(context)).also {
            interpreter = it
        }
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("model.tflite")
        val inputStream = fileDescriptor.createInputStream()
        val channel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return channel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
