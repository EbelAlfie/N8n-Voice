package com.app.customerservice.data.event

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EventParser {
  private val gson = Gson()

  fun parseEvent(text: String): SocketEvent {
    val eventMap = gson.fromJson(text, SocketEvent::class.java)

    val realClass = getEventClass(eventMap)
    val message = gson.fromJson(text, realClass)
    return message
  }

  private fun getEventClass(event: SocketEvent): TypeToken<out SocketEvent>? {
    return when(event.type) {
      EventType.CREATE_CALL -> object: TypeToken<TriggerCallCreation>() {}
      EventType.INCOMING_MESSAGE -> object:  TypeToken<IncomingMessage>() {}
      EventType.INCOMING_CALL -> object: TypeToken<IncomingCall>() {}
      else -> null
    }
  }
}