package com.app.customerservice.data.model

sealed interface ApiResult<out type> {
  data object Loading: ApiResult<Nothing>
  data class Success<type>(val data: type): ApiResult<type>
  data class Error(val ex: Throwable): ApiResult<Nothing>
}