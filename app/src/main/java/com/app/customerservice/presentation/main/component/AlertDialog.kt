package com.app.customerservice.presentation.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.app.customerservice.presentation.main.CallState

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