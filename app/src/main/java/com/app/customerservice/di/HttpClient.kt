package com.app.customerservice.di

import com.app.customerservice.data.service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object HttpClient {

  private val retrofit: Retrofit by lazy {
    val okHttp = OkHttpClient
      .Builder()
      .build()

    Retrofit.Builder()
      .client(okHttp)
      .build()
  }

  fun provideApiService(): ApiService {
    return retrofit.create(ApiService::class.java)
  }
}