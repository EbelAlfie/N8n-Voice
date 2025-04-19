package com.app.customerservice.presentation.call

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.app.customerservice.presentation.main.VoiceViewModel
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RingingState.Active
import io.getstream.video.android.core.RingingState.Idle
import io.getstream.video.android.core.RingingState.Incoming
import io.getstream.video.android.core.RingingState.Outgoing
import io.getstream.video.android.core.RingingState.RejectedByAll
import io.getstream.video.android.core.RingingState.TimeoutNoAnswer

@Composable
fun HumanCallScreen(call: Call, viewModel: VoiceViewModel) {
  val callState by call.state.ringingState.collectAsState()

  when (callState) {
    is Incoming -> RingingContent(call, viewModel)
    is Outgoing -> DialContent(call, viewModel)
    Active -> CallContent(call, viewModel)
    Idle -> {}
    RejectedByAll -> {}
    TimeoutNoAnswer -> {}
  }
}