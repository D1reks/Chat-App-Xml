package com.example.chatfirebase.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.chatfirebase.R
import com.example.chatfirebase.data.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userId = intent.getStringExtra("userId")
        val firebaseRepo = FirebaseRepository()

        val emailTextView = findViewById<TextView>(R.id.userEmailText)
        val userNameTextView = findViewById<TextView>(R.id.userNameText)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Показываем прогресс-бар перед загрузкой
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val (email, name) = firebaseRepo.loadUserData(userId)
                // Обновляем UI
                emailTextView.text = email
                userNameTextView.text = name
            } catch (e: Exception) {
                // Обработка ошибок
                userNameTextView.text = "Ошибка загрузки"
                emailTextView.text = "Ошибка загрузки"
            } finally {
                // Скрываем прогресс-бар после загрузки
                progressBar.visibility = View.GONE
            }
        }
    }
}