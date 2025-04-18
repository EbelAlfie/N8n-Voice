package com.app.customerservice.data.service

import com.app.customerservice.domain.MessageRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

  @POST("webhook-test/send")
  suspend fun sendMessage(
    @Body body: MessageRequest
  ): MessageRequest

}