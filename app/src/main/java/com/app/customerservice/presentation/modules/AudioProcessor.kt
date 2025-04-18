package com.app.customerservice.presentation.modules

import android.Manifest.permission.RECORD_AUDIO
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaCodecInfo.CodecProfileLevel
import android.media.MediaFormat
import android.media.MediaRecorder.AudioSource
import android.os.Environment
import androidx.annotation.RequiresPermission
import com.app.customerservice.presentation.AacDecoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.log10


class AudioProcessor {
  //Default const
  private val SAMPLE_RATE = 44100 //sample rate 44100Hz
  private val CHANNEL = AudioFormat.CHANNEL_IN_DEFAULT //All dev mono??
  private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT //Why not 16 bit?

  private val fileState = MutableStateFlow<File?>(null)

  private var isRecording: Boolean = false
  private val defaultBufferSize =
    AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT)

  private var audioRecorder: AudioRecord? = null
  private var mediaCodec: MediaCodec? = null

  private var recordingJob: Job? = null

  private fun createCodecDecoder(): MediaCodec {
    val name = "audio/mp4a-latm"
    return MediaCodec.createEncoderByType(name).apply {
      val mediaFormat = MediaFormat().apply {
        setString(MediaFormat.KEY_MIME, name)
        setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE)
        setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1)
        setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, defaultBufferSize)
        setInteger(MediaFormat.KEY_BIT_RATE, 32000)
        setInteger(MediaFormat.KEY_AAC_PROFILE, CodecProfileLevel.AACObjectLC)
      }
      configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }
  }

  @RequiresPermission(RECORD_AUDIO)
  fun recordAudio() {
    audioRecorder =
      AudioRecord(AudioSource.MIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT, defaultBufferSize)
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
    while (true) {
      audioRecorder?.let {
        val audioData = ByteArray(1024)
        val len = it.read(audioData, 0, 1024)

        val db = 20 * log10((abs(audioData.get(0).toFloat()) / 32768) / 20e-6f)

        println("VIS LOG $len")
        if (len < 0) return@let

        val processedAudio = processAudio(audioData)
        if (processedAudio != null) {
          recorderBytes += len
          byteArray += processedAudio
        }

      } ?: break

      if (!isRecording) {
        onRecordingStop()
        break
      }
    }

    saveAudio(byteArray)
  }

  private fun processAudio(byteArray: ByteArray): ByteArray? {
    return mediaCodec?.let {
      val timeout = 15000L

      val inputBufferIndex = it.dequeueInputBuffer(timeout)
      if (inputBufferIndex < 0) return@let null

      val inputBuffer = it.getInputBuffer(inputBufferIndex)
      inputBuffer?.clear()
      inputBuffer?.put(byteArray)
      it.queueInputBuffer(
        inputBufferIndex,
        0,
        byteArray.size,
        0,
        0
      )

      val bufferInfo = BufferInfo()
      val outputBufferIndex = it.dequeueOutputBuffer(bufferInfo, timeout)

      if (outputBufferIndex < 0) return@let null

      return@let try {
        val outputBuffer = it.getOutputBuffer(outputBufferIndex)?.apply {
          position(bufferInfo.offset)
          limit(bufferInfo.offset + bufferInfo.size)
        } ?: return@let null
        if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
          val header: ByteArray = AacDecoder().getAdtsHeader(bufferInfo.size - bufferInfo.offset)
          val data = ByteArray(outputBuffer.remaining())
          outputBuffer.get(data)
          header + data
        } else null
      } catch (e: Exception) {
        println("VIS LOG decode error")
        null
      } finally {
        it.releaseOutputBuffer(outputBufferIndex, false)
      }

    }
  }

  private fun saveAudio(buffer: ByteArray) {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val fileName = "${dir.absolutePath}/audio.aac"
    try {
      val outputStream = FileOutputStream(fileName)
      outputStream.write(buffer)
      outputStream.flush()
      outputStream.close()
    } catch (error: Exception) {
      println("VIS LOG error output stream $error")
    }
  }

  fun stopRecording() {
    isRecording = false
  }

  private fun onRecordingStop() {
    mediaCodec?.stop()
    mediaCodec?.release()
    recordingJob?.cancel("Recording Stopped")
    audioRecorder?.stop()
  }

}