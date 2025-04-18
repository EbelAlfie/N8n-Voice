package com.app.customerservice.presentation.modules

import com.app.customerservice.data.event.SocketEvent
import com.app.customerservice.data.event.SocketMessage
import com.app.customerservice.data.socket.SocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventProcessor(
  private val socketService: SocketService = SocketService()
) {
  private val eventListener = mutableSetOf<EventListener<SocketEvent>>()

  fun openConnection() {
    socketService.openConnection()
  }

  fun observeMessages(scope: CoroutineScope) {
    scope.launch {
      socketService.listen().collectLatest {
        when (it) {
          is SocketMessage.Event -> onEvent(it.message)
          is SocketMessage.Error -> {}
        }
      }
    }
  }

  private fun onEvent(event: SocketEvent) {
    eventListener.forEach { listener -> listener.onEvent(event) }
  }

  fun addEventListener(onEvent: (SocketEvent) -> Unit) {
    eventListener.add(object: EventListener<SocketEvent> {
      override fun onEvent(event: SocketEvent) { onEvent(event) }
    })
  }

  fun addEventListener(eventType: Class<SocketEvent>, onEvent: (SocketEvent) -> Unit) {
    val filter = { event: SocketEvent -> event.javaClass == eventType }

    eventListener.add(object: EventListener<SocketEvent> {
      override fun onEvent(event: SocketEvent) {
        if (filter(event)) onEvent(event)
      }
    })
  }

  fun addEventListener(vararg events: Class<out SocketEvent>, onEvent: (SocketEvent) -> Unit) {
    val filter = { event: SocketEvent -> event.javaClass in events }

    eventListener.add(object: EventListener<SocketEvent> {
      override fun onEvent(event: SocketEvent) {
        if (filter(event)) onEvent(event)
      }
    })
  }

}