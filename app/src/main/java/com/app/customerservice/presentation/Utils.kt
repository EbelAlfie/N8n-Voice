package com.app.customerservice.presentation

import android.media.MediaCodecInfo.CodecProfileLevel

class AacDecoder {

  fun getAdtsHeader(len: Int): ByteArray {
    val sampleRateIndex = 4
    val channel = 1
    val frameLength: Int = len + 7
    val adtsHeader = ByteArray(7)

    adtsHeader[0] = 0xFF.toByte() // Sync Word
    adtsHeader[1] = 0xF1.toByte() // MPEG-4, Layer (0), No CRC
    adtsHeader[2] = ((CodecProfileLevel.AACObjectLC - 1) shl 6).toByte()
    adtsHeader[2] = (adtsHeader[2].toInt() or ((sampleRateIndex.toByte()).toInt() shl 2)).toByte()
    adtsHeader[2] = (adtsHeader[2].toInt() or ((channel.toByte()).toInt() shr 2)).toByte()
    adtsHeader[3] = ((((channel and 3) shl 6) or ((frameLength shr 11) and 0x03)).toByte())
    adtsHeader[4] = ((frameLength shr 3) and 0xFF).toByte()
    adtsHeader[5] = (((frameLength and 0x07) shl 5) or 0x1f).toByte()
    adtsHeader[6] = 0xFC.toByte()

    return adtsHeader
  }
}