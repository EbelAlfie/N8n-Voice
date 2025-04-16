package com.app.customerservice.data.model

import com.google.gson.annotations.SerializedName

open class SocketEvent(
  @SerializedName("type")
  val type: String
)

data class IncomingCall(
  @SerializedName("from")
  val from: String
): SocketEvent(type = EventType.INCOMING_CALL)

data class IncomingMessage(
  @SerializedName("from")
  val from: String,
  @SerializedName("content")
  val content: String
): SocketEvent(type = EventType.INCOMING_MESSAGE)