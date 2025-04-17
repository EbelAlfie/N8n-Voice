package com.app.customerservice.data.event

sealed interface SocketMessage<out T> {
  data class Event<T>(
    val message: T
  ): SocketMessage<T>

  data class Error(
    val error: Throwable
  ): SocketMessage<Nothing>
}
