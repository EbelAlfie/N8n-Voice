package com.app.customerservice.presentation.main.component

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
fun rememberSpeechToTextIntent(): Intent {
  val intent = remember {
    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
      )
      putExtra(
        RecognizerIntent.EXTRA_LANGUAGE,
        Locale.getDefault()
      )
      putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
    }
  }

  return intent
}