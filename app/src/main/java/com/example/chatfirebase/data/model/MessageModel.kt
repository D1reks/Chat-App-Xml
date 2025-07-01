package com.example.chatfirebase.data.model

data class UserMessage(
    val senderId: String,
    val reciverId: String,
    val text: String,
)