package com.p1neapplexpress.telegrec.audio

import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import kotlin.math.max
import kotlin.math.min

class Wave(private val file: File) {

    /**
     * Constructs header for wav file format
     */
    fun writeHeader() {
        val headerSize = 44
        val header = ByteArray(headerSize)

        header[0] = 'R'.code.toByte() // RIFF/WAVE header
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()

        header[4] = (0 and 0xff).toByte() // Size of the overall file, 0 because unknown
        header[5] = (0 shr 8 and 0xff).toByte()
        header[6] = (0 shr 16 and 0xff).toByte()
        header[7] = (0 shr 24 and 0xff).toByte()

        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        header[12] = 'f'.code.toByte() // 'fmt ' chunk
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()

        header[16] = 16 // Length of format data
        header[17] = 0
        header[18] = 0
        header[19] = 0

        header[20] = 1 // Type of format (1 is PCM)
        header[21] = 0

        header[22] = NUMBER_CHANNELS.toByte()
        header[23] = 0

        header[24] = (RECORDER_SAMPLE_RATE and 0xff).toByte() // Sampling rate
        header[25] = (RECORDER_SAMPLE_RATE shr 8 and 0xff).toByte()
        header[26] = (RECORDER_SAMPLE_RATE shr 16 and 0xff).toByte()
        header[27] = (RECORDER_SAMPLE_RATE shr 24 and 0xff).toByte()

        header[28] =
            (BYTE_RATE and 0xff).toByte() // Byte rate = (Sample Rate * BitsPerSample * Channels) / 8
        header[29] = (BYTE_RATE shr 8 and 0xff).toByte()
        header[30] = (BYTE_RATE shr 16 and 0xff).toByte()
        header[31] = (BYTE_RATE shr 24 and 0xff).toByte()

        header[32] = (NUMBER_CHANNELS * BITS_PER_SAMPLE / 8).toByte() //  16 Bits stereo
        header[33] = 0

        header[34] = BITS_PER_SAMPLE.toByte() // Bits per sample
        header[35] = 0

        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()

        header[40] = (0 and 0xff).toByte() // Size of the data section.
        header[41] = (0 shr 8 and 0xff).toByte()
        header[42] = (0 shr 16 and 0xff).toByte()
        header[43] = (0 shr 24 and 0xff).toByte()

        file.writeBytes(header)
    }

    fun updateHeader() {
        val dataArr = ByteArray(128)
        FileInputStream(file).use {
            it.read(dataArr, 0, 128)
        }

        val data: ArrayList<Byte> = dataArr.toList() as ArrayList<Byte>
        updateHeaderInformation(data, file.length())

        RandomAccessFile(file, "rw").use {
            it.seek(0)
            it.write(data.toByteArray())
        }
    }

    private fun updateHeaderInformation(data: ArrayList<Byte>, fileSize: Long) {
        val contentSize = fileSize - 44

        data[4] = (fileSize and 0xff).toByte() // Size of the overall file
        data[5] = (fileSize shr 8 and 0xff).toByte()
        data[6] = (fileSize shr 16 and 0xff).toByte()
        data[7] = (fileSize shr 24 and 0xff).toByte()

        data[40] = (contentSize and 0xff).toByte() // Size of the data section.
        data[41] = (contentSize shr 8 and 0xff).toByte()
        data[42] = (contentSize shr 16 and 0xff).toByte()
        data[43] = (contentSize shr 24 and 0xff).toByte()
    }

    fun waveform(): Pair<ShortArray,ShortArray> {

        fun downsample(samples: ShortArray): Pair<ShortArray,ShortArray> {
            val downsampledSamplesMin = ShortArray(128)
            val downsampledSamplesMax = ShortArray(128)

            val step = samples.size / 128

            for (i in 0 until 128) {
                var max: Short = 0
                var min:Short=0
                for (j in i * step until (i + 1) * step) {
                    max = max(max.toInt(), samples[j].toInt()).toShort()
                    min = min(max.toInt(), samples[j].toInt()).toShort()
                }
                downsampledSamplesMax[i] = max
                downsampledSamplesMin[i] = min
            }

            return downsampledSamplesMax to downsampledSamplesMin
        }

        val shortList = mutableListOf<Short>()
        val buffer = ByteArray(1024 * 1024 * 64)

        FileInputStream(file).use { inputStream ->
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                for (i in 0 until bytesRead / 2) {
                    val sample = ((buffer[i * 2 + 1].toInt() and 0xFF) shl 8) or (buffer[i * 2].toInt() and 0xFF)
                    shortList.add(sample.toShort())
                }
            }
        }

        return downsample(shortList.toShortArray())
    }



    companion object {
        const val RECORDER_SAMPLE_RATE = 48000
        const val BITS_PER_SAMPLE: Short = 16
        const val NUMBER_CHANNELS: Short = 1
        const val BYTE_RATE = (RECORDER_SAMPLE_RATE * BITS_PER_SAMPLE * NUMBER_CHANNELS) / 8
    }
}