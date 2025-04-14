package com.app.customerservice.presentation.main

import com.twilio.voice.Call

sealed class CallState {

  data object Idle: CallState()

  data object Connecting: CallState()

  data class Connected(val call: Call): CallState()

  data class Error(val error: Throwable?): CallState()

}