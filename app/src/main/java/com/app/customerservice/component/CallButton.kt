package com.app.customerservice.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CallButton(
  modifier: Modifier = Modifier,
  text: String = "Call",
  onClick: () -> Unit
) {
  Button(
    modifier = modifier,
    onClick = onClick
  ) {
    Text(text = text, color = Color.Blue)
  }
}