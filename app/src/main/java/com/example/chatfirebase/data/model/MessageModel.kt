package com.example.chatfirebase.data.model

data class UserMessage(
    val messageId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long
)