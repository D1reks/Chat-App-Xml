package com.example.chatfirebase.ui.chat

import FirebaseRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatfirebase.R
import com.example.chatfirebase.data.chat_recycler_adapters.ChatFragmentAdapter
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.data.util.UIUtil.updateTextViewText
import kotlinx.coroutines.launch

class ChatFragment : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var _adapter: ChatFragmentAdapter
    private lateinit var _firebaseRepo: FirebaseRepository
    private var _messages: MutableList<UserMessage> = mutableListOf()
    private var _userId: String? = null
    private var _userName: String? = null
    private var _userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_fragment)

        _firebaseRepo = FirebaseRepository()
        recyclerView = findViewById(R.id.recyclerMessagesList)
        _userName = intent.getStringExtra("reciverName")
        _userEmail = intent.getStringExtra("reciverEmail")

        val sendButton = findViewById<Button>(R.id.sendMessageButt)
        val editTextMess = findViewById<EditText>(R.id.writeTextMessage)

        val safeUserId = _userId ?: ""
        val safeUserName = _userName ?: ""
        val safeUserEmail = _userEmail ?: ""


        _adapter = ChatFragmentAdapter(_messages, safeUserId)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = _adapter


        lifecycleScope.launch {
            try {
                val loadedMessages = _firebaseRepo.loadChatFragment(safeUserEmail)
                _messages.clear()
                _messages.addAll(loadedMessages)
                println("Loaded messages: ${loadedMessages.size}")
                runOnUiThread {
                    _adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }

        updateTextViewText(this, R.id.recipientNameText, _userName)

        sendButton.setOnClickListener {
            val messageText = editTextMess.text.toString()
            val message = UserMessage(safeUserId, safeUserEmail, safeUserName, messageText)

            _firebaseRepo.sendMessage(this, message)
            _messages.add(message)
            _adapter.notifyItemInserted(_messages.size - 1)
        }
    }
}