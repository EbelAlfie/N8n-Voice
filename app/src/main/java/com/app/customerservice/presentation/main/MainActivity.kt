package com.app.customerservice.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.customerservice.presentation.modules.AudioProcessor
import com.app.customerservice.presentation.theme.CustomerServiceTheme

class MainActivity : ComponentActivity() {

  private val viewModel: VoiceViewModel by lazy { VoiceViewModel() }

  private val audioProcessor by lazy { AudioProcessor() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    registerCallIntent()
    setupView()
  }

  private fun setupView() {
    enableEdgeToEdge()
    setContent {
      CustomerServiceTheme {
        MainScreen(viewModel, audioProcessor)
      }
    }
  }

  private fun registerCallIntent() {

  }
}