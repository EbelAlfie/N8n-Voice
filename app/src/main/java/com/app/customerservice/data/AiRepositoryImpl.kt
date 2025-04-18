package com.app.customerservice.data

import com.app.customerservice.data.service.ApiService
import com.app.customerservice.domain.AiRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AiRepositoryImpl(
  private val apiService: ApiService
): AiRepository {
  override fun sendMessage() {
    val file = File("")
    val audio = file.asRequestBody("audio/*".toMediaTypeOrNull())
    apiService.sendMessage(audio)
  }
}