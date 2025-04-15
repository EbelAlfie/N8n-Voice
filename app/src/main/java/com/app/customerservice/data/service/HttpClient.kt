package com.app.customerservice.data.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit

class HttpClient {

  private fun provideOkHttp(): OkHttpClient {
    return OkHttpClient.Builder()
      .build()
  }

  private fun provideRetrofit(): Retrofit {
    val okHttpClient = provideOkHttp()

    return Retrofit.Builder()
      .client(okHttpClient)
      .build()
  }

  fun provideApiService(): ApiService {
    val retrofit = provideRetrofit()
    return retrofit.create(ApiService::class.java)
  }
}