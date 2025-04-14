package com.app.customerservice.modules

import android.Manifest.permission.RECORD_AUDIO
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaFormat
import android.media.MediaRecorder.AudioSource
import android.os.Environment
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.log10


class AudioProcessor {
  //Default const
  private val SAMPLE_RATE = 44100 //sample rate 44100Hz
  private val CHANNEL = AudioFormat.CHANNEL_IN_DEFAULT //All dev mono??
  private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT //Why not 16 bit?

  private var isRecording: Boolean = false
  private val defaultBufferSize =
    AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT) * 2

  private var audioRecorder: AudioRecord? = null
  private var mediaCodec: MediaCodec? = null

  private var recordingJob: Job? = null

  private fun createCodecDecoder(): MediaCodec {
    return MediaCodec.createDecoderByType("audio/mp4a-latm").apply {
      val mediaFormat = MediaFormat().apply {
        setString(MediaFormat.KEY_MIME, "audio/mp4a-latm")
        setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE)
        setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1)
      }
      configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }
  }

  @RequiresPermission(RECORD_AUDIO)
  fun recordAudio() {
    audioRecorder = AudioRecord(AudioSource.MIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT, defaultBufferSize)
    mediaCodec = createCodecDecoder().also {
      it.start()
    }

    startRecord()
  }

  private fun startRecord() {
    isRecording = true
    observeRecording()
    audioRecorder?.startRecording()
  }

  private fun observeRecording() {
    recordingJob = CoroutineScope(Dispatchers.IO).launch { recordAudioBytes() }
  }

  private fun recordAudioBytes() {
    var recorderBytes = 0
    var byteArray = byteArrayOf()
    while(isRecording) {
      audioRecorder?.let {
        val audioData = ByteArray(defaultBufferSize)
        val len = it.read(audioData, 0, defaultBufferSize)
        recorderBytes += len
        byteArray += audioData

        val db = 20 * log10( (abs(audioData.get(0).toFloat()) /32768) / 20e-6f)
        println("VIS LOG ${len} ${db}")
      }

      processAudio(byteArray)
    }
  }

  private fun processAudio(byteArray: ByteArray) {
    val bufferInfo = BufferInfo()
    val codecInputBuffers = mediaCodec?.dequeueInputBuffer()
    val codecOutputBuffers = mediaCodec!!.outputBuffers
//    val newByte = decodeToMp3(byteArray)
    saveAudio(byteArray)
  }

  private fun saveAudio(buffer: ByteArray) {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val fileName = "${dir.absolutePath}/audio.pcm"
    try {
      val outputStream = FileOutputStream(fileName)
      outputStream.write(buffer)
      outputStream.flush()
      outputStream.close()
      println("VIS LOG FILE SAVED!")
    } catch (error: Exception) {
      println("VIS LOG error output stream $error")
    }
  }

  fun stopRecording() {
    isRecording = false
    recordingJob?.cancel("Recording Stopped")
    audioRecorder?.stop()
  }

}