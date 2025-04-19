package com.app.customerservice.data.socket

import com.app.customerservice.data.event.EventParser
import com.app.customerservice.data.event.SocketEvent
import com.app.customerservice.data.event.SocketMessage
import com.app.customerservice.di.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.nio.charset.Charset

class SocketService: WebSocketListener() {
  private var webSocket: WebSocket? = null

  private val _eventFlow = MutableSharedFlow<SocketMessage<SocketEvent>>(replay = 1)

  private val eventParser = EventParser()

  fun openConnection() {
    webSocket = HttpClient.provideWebSocket(this)
  }

  override fun onOpen(webSocket: WebSocket, response: Response) {
    super.onOpen(webSocket, response)
  }

  override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
    super.onMessage(webSocket, bytes)
    println("Raw message ${bytes}")
    val message = parseMessage(bytes.string(Charset.defaultCharset()))
    _eventFlow.tryEmit(SocketMessage.Event(message))
  }

  override fun onMessage(webSocket: WebSocket, text: String) {
    super.onMessage(webSocket, text)
    val message = parseMessage(text)
    println("message ${message}")
    _eventFlow.tryEmit(SocketMessage.Event(message))
  }

  override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    super.onFailure(webSocket, t, response)
    _eventFlow.tryEmit(SocketMessage.Error(t))
  }

  override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
    super.onClosed(webSocket, code, reason)
    //TODO
  }

  override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
    super.onClosing(webSocket, code, reason)
    //TODO
  }

  fun send(text: String) = webSocket?.send(text)

  fun listen() = _eventFlow.asSharedFlow()

  private fun parseMessage(text: String): SocketEvent {
    val parsedEvent = eventParser.parseEvent(text)
    return parsedEvent
  }
}