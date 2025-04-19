package com.app.customerservice.data.event

import com.google.gson.annotations.SerializedName

open class SocketEvent(
  @SerializedName("type")
  val type: String
)

data class IncomingCall(
  @SerializedName("from")
  val from: String
): SocketEvent(type = EventType.INCOMING_CALL)

data object TriggerCallCreation: SocketEvent(type = EventType.CREATE_CALL)

data class IncomingMessage(
  @SerializedName("message")
  val message: String
): SocketEvent(type = EventType.INCOMING_MESSAGE)