package com.app.customerservice.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.app.customerservice.presentation.main.CallState.Connected
import com.app.customerservice.presentation.main.CallState.Connecting
import com.app.customerservice.presentation.main.CallState.Error
import com.app.customerservice.presentation.main.CallState.Idle
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.ConnectOptions
import com.twilio.voice.Voice
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
  @ApplicationContext private val applicationContext: Context
): ViewModel(), Call.Listener {

  private val _callState = MutableStateFlow<CallState>(Idle)
  val callState: StateFlow<CallState> = _callState.asStateFlow()

  private val accessToken = ""

  fun connectCall() {
    val params = HashMap<String, String>().apply {
      put("to", "+62 897 8579 751")
    }

    val option = ConnectOptions
      .Builder(accessToken)
      .params(params)
      .build()
    Voice.connect(applicationContext, option, this)
  }

  override fun onConnectFailure(call: Call, callException: CallException) {
    _callState.update { Error(callException) }
  }

  override fun onRinging(call: Call) {

  }

  override fun onConnected(call: Call) {
    _callState.update { Connected(call) }
  }

  override fun onReconnecting(call: Call, callException: CallException) {
    _callState.update { Connecting }
  }

  override fun onReconnected(call: Call) {

  }

  override fun onDisconnected(call: Call, callException: CallException?) {
    _callState.update { Error(callException) }
  }
}