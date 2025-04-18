package com.app.customerservice.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.customerservice.presentation.main.component.CallButton
import com.app.customerservice.presentation.modules.AudioProcessor

@Composable
fun MainScreen(
  viewModel: VoiceViewModel,
  audioProcessor: AudioProcessor
) {
  val callState by viewModel.callState.collectAsState()

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier.fillMaxSize().padding(innerPadding),
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