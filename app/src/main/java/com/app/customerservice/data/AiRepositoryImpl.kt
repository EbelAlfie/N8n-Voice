package com.app.customerservice.data

import com.app.customerservice.data.model.ApiResult
import com.app.customerservice.data.service.ApiService
import com.app.customerservice.domain.AiRepository
import com.app.customerservice.domain.MessageRequest
import com.app.customerservice.presentation.App.Companion.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AiRepositoryImpl(
  private val apiService: ApiService
) : AiRepository {
  override suspend fun sendMessage(message: String): Flow<ApiResult<MessageRequest>> {
    return flow {
      emit(ApiResult.Loading)
      try {
        val request = MessageRequest(
          uid = User.id,
          message = message
        )
        val response = apiService.sendMessage(request)
        emit(ApiResult.Success(response))
      } catch (error: Throwable) {
        println("Http Error $error")
        emit(ApiResult.Error(error))
      }
    }
  }
}