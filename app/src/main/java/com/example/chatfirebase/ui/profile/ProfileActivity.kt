package com.example.chatfirebase.ui.profile

import FirebaseRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.example.chatfirebase.R
import com.example.chatfirebase.data.util.NavigationUtil.openNextIntent
import com.example.chatfirebase.data.util.UIUtil.updateTextViewText
import com.example.chatfirebase.ui.chat.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val _firebaseRepo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.getStringExtra("userId")

        val progressBar = findViewById<ProgressBar>(R.id.progressBarProfile)
        val buttToChatList = findViewById<Button>(R.id.chatListButton)

        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val (email, name) = _firebaseRepo.loadUserData(userId)

                updateTextViewText(this@ProfileActivity, R.id.userNameText, name)
                updateTextViewText(this@ProfileActivity, R.id.userEmailText, email)
            } catch (e: Exception) {
                e.message?.let { println(it) }

            } finally {
                progressBar.visibility = View.GONE
            }
        }

        buttToChatList.setOnClickListener {
            openNextIntent(this, ChatActivity::class.java, userId)
        }
    }
}