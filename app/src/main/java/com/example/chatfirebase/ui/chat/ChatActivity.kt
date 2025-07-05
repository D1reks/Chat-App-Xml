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
import com.example.chatfirebase.data.util.NavigationUtil.openChatIntent
import com.example.chatfirebase.data.util.UIUtil.showCustomDialog
import com.example.chatfirebase.data.util.UIUtil.showToast
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var _adapter: ChatListAdapter
    private lateinit var _firebaseRepo: FirebaseRepository
    private var _users = mutableListOf<ChatUser>()
    private var _userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        _userId = intent.getStringExtra("userId")
        _firebaseRepo = FirebaseRepository()

        val button: Button = findViewById(R.id.buttonAddChat)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewChats)
        recyclerView.layoutManager = LinearLayoutManager(this)


        _adapter = ChatListAdapter(this, _users) { user ->

            showToast(this, "Вы выбрали: ${user.userName}")
            openChatIntent(this, ChatFragment::class.java,_userId,user.userEmail, user.userName)
        }

        recyclerView.adapter = _adapter

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