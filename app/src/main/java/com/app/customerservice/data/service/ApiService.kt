package com.app.customerservice.data.service

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

  @POST("webhook-test/message")
  fun sendMessage(
    @Body body: RequestBody
  )

}