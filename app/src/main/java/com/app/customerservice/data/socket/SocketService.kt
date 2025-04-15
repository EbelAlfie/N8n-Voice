package com.app.customerservice.data.socket

import com.app.customerservice.data.model.Event
import com.app.customerservice.di.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class SocketService: WebSocketListener() {
  private val webSocket: WebSocket by lazy { HttpClient.provideWebSocket(this) }

  private val _eventFlow = MutableSharedFlow<Event>(replay = 1)

  override fun onOpen(webSocket: WebSocket, response: Response) {
    super.onOpen(webSocket, response)
  }

  override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
    super.onMessage(webSocket, bytes)
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    super.onMessage(webSocket, text)
    val message = parseMessage(text)
    _eventFlow.tryEmit(message)
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    super.onFailure(webSocket, t, response)
  }

  override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
    super.onClosed(webSocket, code, reason)
  }

  override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    super.onClosing(webSocket, code, reason)
  }

  fun send(text: String) = webSocket.send(text)

  fun listen() = _eventFlow.asSharedFlow()

  private fun parseMessage(text: String) {

  }
}