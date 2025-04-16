package com.app.customerservice.data.socket

import com.app.customerservice.data.model.IncomingCall
import com.app.customerservice.data.model.IncomingMessage
import com.app.customerservice.data.model.SocketEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventHandler {
  private val gson = Gson()

  fun parseEvent(text: String): SocketEvent {
    val eventMap = gson.fromJson(text, SocketEvent::class.java)

    val realClass = getEventClass(eventMap)
    val message = gson.fromJson(text, realClass)
    return message
  }

  private fun getEventClass(event: SocketEvent): TypeToken<out SocketEvent>? {
    return when(event) {
      is IncomingCall -> object: TypeToken<IncomingCall>() {}
      is IncomingMessage -> object:  TypeToken<IncomingMessage>() {}
      else -> null
    }
  }
}