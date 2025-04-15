package com.app.customerservice.di

import com.app.customerservice.data.AiRepositoryImpl

object Dependency {
  val aiRepository = AiRepositoryImpl(HttpClient.provideApiService())
}