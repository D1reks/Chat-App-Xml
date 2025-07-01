package com.example.chatfirebase.ui.chat

import ChatListAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatfirebase.R
import com.example.chatfirebase.data.model.ChatUser

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewChats)
        val users = mutableListOf<ChatUser>()
        users.add(ChatUser("Иван Иванов", "ivan@example.com", "@drawable/chat_list_icon"))
        users.add(ChatUser("Петр Петров", "petr@example.com", "@drawable/chat_list_icon"))

        val adapter = ChatListAdapter(this, users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}