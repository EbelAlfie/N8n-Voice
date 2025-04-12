package com.app.customerservice.modules

import android.Manifest.permission
import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ShortBuffer

class AudioProcessor {

  private var audioRecorder: AudioRecord? = null

  @RequiresPermission(RECORD_AUDIO)
  fun recordAudio() {
    val format = AudioFormat
      .Builder()
      .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
      .build()

    audioRecorder = AudioRecord
      .Builder()
      .setAudioSource(MediaRecorder.AudioSource.MIC)
      .setAudioFormat(format)
      .build()

    startRecord()
  }

  private fun startRecord() {
    observeRecording()
    audioRecorder?.startRecording()
  }

  private fun observeRecording() {
    CoroutineScope(Dispatchers.IO).launch {
      while(true) {
        audioRecorder?.let {
          val audioData = ByteBuffer.allocateDirect(100)
          val result = it.read(audioData, 10)
          println("VIS LOG ${result} ${audioData.get(0)}")
        }
      }
    }
  }

  fun stopRecording() {
    audioRecorder?.stop()
  }

}