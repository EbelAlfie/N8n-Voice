package com.app.customerservice.presentation.modules

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberGoogleTextToSpeech(onAudioAvailable: (ArrayList<String>) -> Unit): ManagedActivityResultLauncher<Intent, ActivityResult> {
  return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

    val data = it.data?.extras ?: return@rememberLauncherForActivityResult
    val result: ArrayList<String> = data.getStringArrayList(RecognizerIntent.EXTRA_RESULTS) ?: return@rememberLauncherForActivityResult

    onAudioAvailable(result)
  }
}