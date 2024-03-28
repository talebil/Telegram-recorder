package com.p1neapplexpress.telegrec.audio

import java.io.File
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.Stack

class AudioCaptureThread(val dst: File) : Thread(), Runnable {

    val bufferStack = Stack<ByteBuffer>()
    private var waveFile = Wave(dst)

    init {
        File(dst.parent!!).mkdirs()
        dst.createNewFile()
    }

    override fun run() {
        waveFile.writeHeader()

        while (!interrupted()) {
            if (bufferStack.empty()) continue

            val byteBuffer = bufferStack.pop()
            val data = Arrays.copyOfRange(
                byteBuffer.array(), byteBuffer.arrayOffset(),
                byteBuffer.capacity() + byteBuffer.arrayOffset()
            )

            dst.appendBytes(data)
        }

        waveFile.updateHeader()
    }
}