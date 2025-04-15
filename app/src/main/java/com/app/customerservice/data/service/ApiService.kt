package com.app.customerservice.data.service

import retrofit2.http.POST

interface ApiService {

  @POST("webhook-test/message")
  fun sendMessage()

}