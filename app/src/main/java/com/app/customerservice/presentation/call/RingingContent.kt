package com.app.customerservice.presentation.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import com.app.customerservice.presentation.call.component.CallControl
import com.app.customerservice.presentation.main.VoiceViewModel
import com.app.customerservice.presentation.theme.VoiceLogo
import io.getstream.video.android.core.Call

@Composable
fun RingingContent(
  call: Call,
  viewModel: VoiceViewModel
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    VoiceLogo()

    CallControl(
      onCallAccepted = { viewModel.acceptCSCall(call) },
      onCallRejected = { viewModel.rejectCSCall(call) }
    )
  }
}