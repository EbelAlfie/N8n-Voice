package com.app.customerservice.modules

import android.Manifest.permission.RECORD_AUDIO
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder.AudioSource
import android.os.Environment
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
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

  private var recordingJob: Job? = null

  @RequiresPermission(RECORD_AUDIO)
  fun recordAudio() {
    audioRecorder =
      AudioRecord(AudioSource.MIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT, defaultBufferSize)

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
      saveAudio(byteArray)
    }
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