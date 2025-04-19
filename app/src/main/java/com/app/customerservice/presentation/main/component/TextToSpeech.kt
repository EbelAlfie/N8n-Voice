package com.app.customerservice.presentation.main.component

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.UUID

@Composable
fun rememberTextToSpeech(text: String) {
  val context = LocalContext.current
  var hasTTSInitialized by remember { mutableStateOf(false) }
  val textToSpeech = remember {
    TextToSpeech(context) {
      hasTTSInitialized = it == TextToSpeech.SUCCESS
      println(it)
    }.apply {
      setOnUtteranceProgressListener(object: UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {

        }

        override fun onDone(utteranceId: String?) {

        }

        override fun onError(utteranceId: String?) {

        }

      })
    }
  }

  DisposableEffect(text, hasTTSInitialized) {
    if (hasTTSInitialized)
      textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString())

    onDispose { if (textToSpeech.isSpeaking) textToSpeech.stop() }
  }
}