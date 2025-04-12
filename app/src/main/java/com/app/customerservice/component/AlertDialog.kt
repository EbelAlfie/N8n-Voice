package com.app.customerservice.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.app.customerservice.CallState
import com.app.customerservice.CallState.Connected
import com.app.customerservice.CallState.Connecting
import com.app.customerservice.CallState.Error
import com.app.customerservice.CallState.Idle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallDialog(
  onDismiss: () -> Unit = {},
  callState: CallState
) {
  BasicAlertDialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(),
    content = {
//      when (callState) {
//        is Connected -> DialogContent(title = "Call Connected", )
//        Connecting -> DialogContent()
//        is Error -> DialogContent()
//        else -> Unit
//      }
    }
  )
}

@Composable
fun DialogContent(
  title: String,
  desc: String
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(15.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = title)

    Text(text = desc)
  }
}