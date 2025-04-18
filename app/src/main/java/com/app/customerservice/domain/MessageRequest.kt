package com.app.customerservice.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MessageRequest(
  @Expose
  @SerializedName("from")
  val from: String,
  @Expose
  @SerializedName("message")
  val message: String
)
