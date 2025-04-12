package com.app.customerservice.modules

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission

class AudioProcessor {

  @RequiresPermission(android.Manifest.permission.RECORD_AUDIO)
  fun recordAudio() {
    val format = AudioFormat
      .Builder()
      .setEncoding(AudioFormat.ENCODING_DEFAULT)
      .build()

    val audioRecord = AudioRecord
      .Builder()
      .setAudioSource(MediaRecorder.AudioSource.MIC)
      .setAudioFormat(format)
      .build()

    audioRecord.startRecording()
  }

  private fun startRecord() {

  }

  fun stopRecording() {

  }

}