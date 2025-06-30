package com.example.chatfirebase.data.model

data class UserMessage(
    val senderId: String,
    val text: String,
    val timestamp: Long
)