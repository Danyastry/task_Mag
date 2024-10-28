package com.example.task_magnise.data.webSocket

import com.example.task_magnise.domain.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MyWebSocketListener(
    private val sharedFlow: MutableSharedFlow<State<String>>,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) :
    WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        scope.launch {
            sharedFlow.emit(State.Success("Connected"))
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        scope.launch {
            val message = bytes.utf8()
            sharedFlow.emit(State.Success(message))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        scope.launch {
            sharedFlow.emit(State.Error("Closing WebSocket: $reason"))
            webSocket.close(1000, null)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        scope.launch {
            sharedFlow.emit(State.Error("WebSocket failure: ${t.message}"))
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        scope.launch {
            sharedFlow.emit(State.Error("Closed $reason"))
        }
    }
}