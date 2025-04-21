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
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.CreateCallOptions
import io.getstream.video.android.core.RingingState
import io.getstream.video.android.core.RingingState.Active
import io.getstream.video.android.core.RingingState.Idle
import io.getstream.video.android.core.RingingState.Incoming
import io.getstream.video.android.core.RingingState.Outgoing
import io.getstream.video.android.core.RingingState.RejectedByAll
import io.getstream.video.android.core.RingingState.TimeoutNoAnswer
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

  private val _callState = MutableStateFlow<CallState>(CallState.CallingAI())
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
        is IncomingCall -> println("INCUMING ${it}")
        is TriggerCallCreation -> joinCSCall()
        is IncomingMessage -> updateCallState { CallState.CallingAI(it.message) }
      }
    }
  }

  private suspend fun observeCallState(ringingState: StateFlow<RingingState>) =
    ringingState.collectLatest {
      when (it) {
        is Incoming -> {}
        is Outgoing -> {}
        Active -> {}
        Idle -> {}
        RejectedByAll -> {}
        TimeoutNoAnswer -> {}
      }
    }

  fun refresh() = updateCallState { CallState.CallingAI() }

  private fun joinCSCall() {
    val streamVideo = StreamVideo.instanceOrNull() ?: return //TODO enhance
    val callId = UUID.randomUUID().toString()
    val call = streamVideo.call("default", callId)

    streamVideo.state.activeCall.value?.leave()

    viewModelScope.launch {
      call.join(
        create = true,
        createOptions = CreateCallOptions(
          memberIds = listOf("customer-service")
        ),
        ring = true
      )
        .onSuccess {
          updateCallState { CallState.CallingCustomerService(call) }
        }
        .onError {
          updateCallState { CallState.Error(it.extractCause()) }
        }

      observeCallState(call.state.ringingState)
    }
  }

  fun endCSCall() {
    viewModelScope.launch {
      (_callState.value as? CallState.CallingCustomerService)?.call?.end()
        ?.onSuccess {
          updateCallState { CallState.CallingAI() }
        }
        ?.onError {
          updateCallState { CallState.CallingAI() }
        }
    }
  }

  fun acceptCSCall(call: Call) {
    viewModelScope.launch { call.accept() }
  }

  fun rejectCSCall(call: Call) {
    viewModelScope.launch { call.reject() }
  }

  private fun updateCallState(newState: CallState.() -> CallState) {
    _callState.update { newState.invoke(it) }
  }
}