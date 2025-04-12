package com.app.customerservice.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CallButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Button(
    modifier = modifier,
    onClick = onClick
  ) {
    Text(text = "Call")
  }
}