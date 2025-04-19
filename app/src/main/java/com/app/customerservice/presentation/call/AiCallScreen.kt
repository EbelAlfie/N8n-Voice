package com.app.customerservice.presentation.call

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.customerservice.presentation.main.CallState
import com.app.customerservice.presentation.main.VoiceViewModel
import com.app.customerservice.presentation.main.component.CallButton
import com.app.customerservice.presentation.main.component.rememberSpeechToTextIntent
import com.app.customerservice.presentation.main.component.rememberTextToSpeech
import com.app.customerservice.presentation.theme.VoiceLogo

@Composable
fun AiCallScreen(
  callState: CallState.CallingAI,
  viewModel: VoiceViewModel
) {
  val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

    val data = it.data?.extras ?: return@rememberLauncherForActivityResult
    val result: ArrayList<String> = data.getStringArrayList(RecognizerIntent.EXTRA_RESULTS) ?: return@rememberLauncherForActivityResult

    viewModel.sendAudioMessage(result[0])
  }

  val sttIntent = rememberSpeechToTextIntent()

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    VoiceLogo()

    CallButton(text = "Talk") { launcher.launch(sttIntent) }
  }

  if (callState.responseMsg.isNotBlank()) rememberTextToSpeech(callState.responseMsg)
}