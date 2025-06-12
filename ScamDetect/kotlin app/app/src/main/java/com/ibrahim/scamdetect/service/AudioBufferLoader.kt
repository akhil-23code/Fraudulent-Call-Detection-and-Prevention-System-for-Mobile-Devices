package com.ibrahim.scamdetect.service

import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object AudioBufferLoader {

    fun load(context: Context, uri: Uri): ByteBuffer {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw Exception("Cannot open URI")
        val rawBytes = inputStream.readBytes()
        inputStream.close()

        // Convert byte[] to ByteBuffer
        val buffer = ByteBuffer.allocateDirect(rawBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(rawBytes)
        buffer.rewind()

        return buffer
    }
}
