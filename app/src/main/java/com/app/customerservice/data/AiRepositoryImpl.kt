package com.app.customerservice.data

import com.app.customerservice.data.service.ApiService
import com.app.customerservice.domain.AiRepository

class AiRepositoryImpl(
  private val apiService: ApiService
): AiRepository {
  override fun sendMessage() {
    apiService.sendMessage()
  }
}