package com.app.customerservice.presentation.modules

import com.app.customerservice.data.event.SocketEvent
import com.app.customerservice.data.event.SocketMessage
import com.app.customerservice.data.socket.SocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

typealias ListenerType = (SocketEvent) -> Unit

class EventProcessor(
  private val socketService: SocketService = SocketService()
) {
  private val eventListener = mutableSetOf<Class<SocketEvent>>()

  fun openConnection() {
    socketService.openConnection()
  }

  fun observeMessages(scope: CoroutineScope) {
    scope.launch {
      socketService.listen().collectLatest {
        when (it) {
          is SocketMessage.EventMessage -> onEvent(it.message)
          is SocketMessage.Error -> {}
        }
      }
    }
  }

  private fun onEvent(event: SocketEvent) {

  }

  fun addEventListener(newListener: ListenerType, type: Class<SocketEvent>) {
    eventListener
  }
}