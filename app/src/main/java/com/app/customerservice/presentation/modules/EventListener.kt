package com.app.customerservice.presentation.modules

import com.app.customerservice.data.event.SocketEvent

interface EventListener<type: SocketEvent> {
  fun onEvent(event: type)
}