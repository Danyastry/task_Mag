package com.example.task_magnise.data.webSocket

import com.example.task_magnise.data.Data.WEB_SOCKET_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class WebSocketClient(
    private val client: OkHttpClient,
    private val listener: MyWebSocketListener
) {
    private var webSocket: WebSocket? = null

    fun connect(token: String) {
        val url = "$WEB_SOCKET_URL$token"
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
    }
}