package com.app.customerservice.data.model

sealed interface SocketMessage<out T> {
  data class EventMessage<T>(
    private val message: T
  ): SocketMessage<T>

  data class Error(
    private val error: Throwable
  ): SocketMessage<Nothing>
}
