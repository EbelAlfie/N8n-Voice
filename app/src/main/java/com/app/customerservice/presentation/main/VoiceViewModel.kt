package com.app.customerservice.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.customerservice.data.event.IncomingCall
import com.app.customerservice.data.event.IncomingMessage
import com.app.customerservice.di.Dependency
import com.app.customerservice.domain.AiRepository
import com.app.customerservice.presentation.main.CallState.Idle
import com.app.customerservice.presentation.modules.EventListener
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

  fun observeMessages() {
    subscribeEvents()
    socketClient.observeMessages(viewModelScope)
  }

  fun sendAudioMessage() {
    aiRepository.sendMessage()
  }

  private fun subscribeEvents() {
    socketClient.addEventListener {
      when (it) {
        is IncomingCall -> {}
        is IncomingMessage -> {}
      }
    }
  }

}