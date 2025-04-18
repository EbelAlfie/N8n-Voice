package com.app.customerservice.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.customerservice.R

@Composable
fun VoiceLogo() {
  Box(
    modifier = Modifier
      .clip(CircleShape)
      .background(Color.Green, CircleShape)
      .padding(10.dp),
  ) {
    Icon(
      modifier = Modifier.size(50.dp),
      painter = painterResource(R.drawable.ic_mic),
      tint = Color.Black,
      contentDescription = null,
    )
  }
}