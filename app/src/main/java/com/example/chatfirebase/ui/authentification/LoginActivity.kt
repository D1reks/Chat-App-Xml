package com.example.chatfirebase.ui.authentification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.chatfirebase.R
import com.example.chatfirebase.data.repository.FirebaseRepository
import com.example.chatfirebase.input_implementation.input_provider.UserInputProvider

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val inputProvider = UserInputProvider(this)
        val firebaseRepo = FirebaseRepository()

        val loginButton = findViewById<Button>(R.id.authButt)
        loginButton.setOnClickListener {
            firebaseRepo.loginUser(inputProvider.getEmail(),inputProvider.getPassword())
        }
    }
}