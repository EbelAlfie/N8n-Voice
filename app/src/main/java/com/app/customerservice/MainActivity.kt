package com.app.customerservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.app.customerservice.component.CallButton
import com.app.customerservice.ui.theme.CustomerServiceTheme

class MainActivity : ComponentActivity() {

  private val viewModel: VoiceViewModel by lazy { VoiceViewModel(applicationContext) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    registerCallIntent()
    setupView()
  }

  private fun setupView() {
    enableEdgeToEdge()
    setContent {
      CustomerServiceTheme {
        CallContent(viewModel)
      }
    }
  }

  private fun registerCallIntent() {

  }
}