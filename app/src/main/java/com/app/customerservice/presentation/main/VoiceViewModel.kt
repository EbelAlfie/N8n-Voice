package com.app.customerservice.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.customerservice.data.event.IncomingCall
import com.app.customerservice.data.event.IncomingMessage
import com.app.customerservice.data.event.TriggerCallCreation
import com.app.customerservice.di.Dependency
import com.app.customerservice.domain.AiRepository
import com.app.customerservice.presentation.modules.EventProcessor
import io.getstream.result.extractCause
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class VoiceViewModel(
  private val socketClient: EventProcessor = EventProcessor(),
  private val aiRepository: AiRepository = Dependency.aiRepository
) : ViewModel() {

  private val _callState = MutableStateFlow<CallState>(CallState.Idle)
  val callState: StateFlow<CallState> = _callState.asStateFlow()

  init {
    socketClient.openConnection()
    observeMessages()
  }

  private fun observeMessages() {
    subscribeEvents()
    socketClient.observeMessages(viewModelScope)
  }

  fun sendAudioMessage(message: String) {
    viewModelScope.launch {
      aiRepository.sendMessage(message).collectLatest {

      }
    }
  }

  private fun subscribeEvents() {
    socketClient.addEventListener {
      when (it) {
        is IncomingCall ->
          println("INCUMING")
        is TriggerCallCreation -> joinCSCall()
        is IncomingMessage -> println("INCUMING MES")
      }
    }
  }

  private fun joinCSCall() {
    val streamVideo = StreamVideo.instanceOrNull() ?: return //TODO enhance
    val callId = UUID.randomUUID().toString()
    val call = streamVideo.call("audio room", callId)

    streamVideo.state.activeCall.value?.leave()

    updateCallState { CallState.Dialing }

    viewModelScope.launch {
      call.join(true)
        .onSuccess {
          updateCallState { CallState.Connected(call) }
        }
        .onError {
          updateCallState { CallState.Error(it.extractCause()) }
        }
    }
  }

  private fun endCSCall() {
    viewModelScope.launch {
      (_callState.value as? CallState.Connected)?.call?.end()
        ?.onSuccess {
          updateCallState { CallState.Idle }
        }
        ?.onError {
          updateCallState { CallState.Idle }
        }
    }
  }

  private fun updateCallState(newState: CallState.() -> CallState) {
    _callState.update { newState.invoke(it) }
  }
}