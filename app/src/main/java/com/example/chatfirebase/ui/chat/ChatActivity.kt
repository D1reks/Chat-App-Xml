package com.example.chatfirebase.ui.chat

import ChatListAdapter
import FirebaseRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatfirebase.R
import com.example.chatfirebase.data.model.ChatUser
import com.example.chatfirebase.data.util.UIUtil.showCustomDialog
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var _adapter: ChatListAdapter
    private var _users = mutableListOf<ChatUser>()
    private lateinit var _firebaseRepo: FirebaseRepository
    private var _userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        _userId = intent.getStringExtra("userId")
        _firebaseRepo = FirebaseRepository()

        // Настройка RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewChats)
        recyclerView.layoutManager = LinearLayoutManager(this)
        _adapter = ChatListAdapter(this, _users)
        recyclerView.adapter = _adapter

        // Настройка кнопки
        val button: Button = findViewById(R.id.buttonAddChat)
        button.setOnClickListener {
            showCustomDialog(this, _userId, this, _users, _adapter)
        }
        lifecycleScope.launch {
            try {
                val loadedUsers = _firebaseRepo.loadChatList(_userId)
                _users.clear()
                _users.addAll(loadedUsers)
                _adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                println(e.message)
            }
        }

    }


}