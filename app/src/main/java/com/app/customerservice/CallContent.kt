package com.app.customerservice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.customerservice.CallState.Connected
import com.app.customerservice.CallState.Connecting
import com.app.customerservice.CallState.Error
import com.app.customerservice.component.CallButton
import com.app.customerservice.component.DialogContent
import kotlinx.coroutines.delay

@Composable
fun CallContent(
  viewModel: VoiceViewModel
) {
  val callState by viewModel.callState.collectAsState()
  var showDialog by remember { mutableStateOf(false) }

  LaunchedEffect(callState) {
    when (callState) {
      !is CallState.Idle -> showDialog = true
      else -> return@LaunchedEffect
    }
    delay(5000L)
    showDialog = false
  }

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier.fillMaxSize().padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      when (callState) {
        is Connected -> DialogContent(
          title = "Call Connected",
          desc = "You are connected to ${(callState as Connected).call.to}"
        )

        Connecting -> DialogContent(title = "Connecting Call", desc = "")
        is Error -> DialogContent(
          title = "Failed initiating a call",
          desc = "Failed to connect ${(callState as Error).error?.message}"
        )

        else -> Unit
      }

      CallButton(
        onClick = viewModel::connectCall
      )
    }
  }
}