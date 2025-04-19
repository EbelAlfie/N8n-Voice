package com.app.customerservice.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.customerservice.presentation.App
import com.app.customerservice.presentation.call.AiCallScreen
import com.app.customerservice.presentation.call.CallContent
import com.app.customerservice.presentation.call.HumanCallScreen
import com.app.customerservice.presentation.main.CallState.CallingCustomerService
import com.app.customerservice.presentation.main.CallState.Error
import com.app.customerservice.presentation.main.CallState.CallingAI
import com.app.customerservice.presentation.main.component.CallButton
import com.app.customerservice.presentation.modules.AudioProcessor
import io.getstream.video.android.core.StreamVideo

@Composable
fun MainScreen(
  viewModel: VoiceViewModel,
  audioProcessor: AudioProcessor
) {
  val callState by viewModel.callState.collectAsState()

  when (callState) {
    is CallingAI ->
      AiCallScreen(callState as CallingAI, viewModel)
    is CallingCustomerService ->
      HumanCallScreen((callState as CallingCustomerService).call, viewModel)
    is Error -> Button(onClick = viewModel::refresh) { Text(text = "Failed") }
  }

  StreamVideoProvider { streamVideo ->
    val ringingCall by streamVideo.state.ringingCall.collectAsState()
    ringingCall?.let { //Has ringing call
      if (it.user.id != App.CUST_ID) CallContent(it, viewModel)
    }
  }
}

@Composable
fun AudioRecordScreen(
  viewModel: VoiceViewModel,
  audioProcessor: AudioProcessor
) {
  val callState by viewModel.callState.collectAsState()

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      CallButton(
        onClick = audioProcessor::recordAudio,
        text = "Record"
      )

      CallButton(
        onClick = audioProcessor::stopRecording,
        text = "Stop"
      )

    }
  }
}

@Composable
fun StreamVideoProvider(content: @Composable (StreamVideo) -> Unit) {
  val streamVideo by StreamVideo.instanceState.collectAsState()
  streamVideo?.let { content(it) }
}