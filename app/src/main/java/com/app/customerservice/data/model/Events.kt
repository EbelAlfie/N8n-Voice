package com.app.customerservice.data.model

import com.google.gson.annotations.SerializedName

open class Event(
  @SerializedName("type")
  val type: String
)

data class IncomingCall(
  @SerializedName("from")
  val from: String
): Event(type = "incoming.call")

data class IncomingMessage(
  @SerializedName("from")
  val from: String,
  @SerializedName("content")
  val content: String
): Event(type = "incoming.message")