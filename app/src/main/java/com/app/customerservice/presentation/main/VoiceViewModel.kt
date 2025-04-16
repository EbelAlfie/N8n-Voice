package com.app.customerservice.presentation.main

import androidx.lifecycle.ViewModel
import com.app.customerservice.di.Dependency
import com.app.customerservice.domain.AiRepository
import com.app.customerservice.presentation.main.CallState.Idle
import com.app.customerservice.presentation.modules.EventProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoiceViewModel (
  private val socketClient: EventProcessor = EventProcessor(),
  private val aiRepository: AiRepository = Dependency.aiRepository
): ViewModel() {

  private val _callState = MutableStateFlow<CallState>(Idle)
  val callState: StateFlow<CallState> = _callState.asStateFlow()

  init {
    socketClient.openConnection()
  }

  fun sendAudioMessage() {
    aiRepository.sendMessage()
  }

}