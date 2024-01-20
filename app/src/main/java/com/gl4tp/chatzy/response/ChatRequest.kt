package com.gl4tp.chatzy.response

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)