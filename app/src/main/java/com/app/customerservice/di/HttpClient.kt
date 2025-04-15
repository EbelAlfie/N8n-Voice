package com.app.customerservice.di

import com.app.customerservice.data.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Retrofit

object HttpClient {

  private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient
      .Builder()
      .build()
  }

  private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl("http://localhost:5678/")
      .build()
  }

  fun provideWebSocket(socketListener: WebSocketListener): WebSocket {
    val request = Request.Builder()
      .url("ws://localhost:8081/")
      .build()
    return okHttpClient.newWebSocket(request, socketListener)
  }

  fun provideApiService(): ApiService {
    return retrofit.create(ApiService::class.java)
  }
}