package com.example.chatfirebase.data.model

data class UserMessage(
    val senderId: String?,
    val reciverEmail: String,
    val reciverName: String,
    val text: String,
)