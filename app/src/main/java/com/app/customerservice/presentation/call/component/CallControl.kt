package com.app.customerservice.presentation.call.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CallControl(
  onCallAccepted: () -> Unit,
  onCallRejected: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceAround
  ) {
    AcceptButton(onCallAccepted)

    RejectButton(onCallRejected)
  }
}

@Composable
fun AcceptButton(
  onAccepted: () -> Unit = {}
) {
  Box(
    modifier = Modifier
      .clip(CircleShape)
      .background(Color.Green, CircleShape)
      .clickable { onAccepted() }
      .padding(10.dp)
  ) {
    Icon(
      imageVector = Icons.Filled.Call,
      tint = Color.White,
      contentDescription = null
    )
  }
}

@Composable
fun RejectButton(
  onRejected: () -> Unit = {}
) {
  Box(
    modifier = Modifier
      .clip(CircleShape)
      .background(Color.Red, CircleShape)
      .clickable { onRejected() }
      .padding(10.dp)
  ) {
    Icon(
      imageVector = Icons.Rounded.Phone,
      tint = Color.White,
      contentDescription = null
    )
  }
}