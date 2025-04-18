package com.app.customerservice.domain

import com.app.customerservice.data.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface AiRepository {
  suspend fun sendMessage(message: String): Flow<ApiResult<MessageRequest>>
}