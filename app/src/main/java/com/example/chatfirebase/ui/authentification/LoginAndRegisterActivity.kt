package com.example.chatfirebase.ui.authentification

import FirebaseRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.chatfirebase.R
import com.example.chatfirebase.input_implementation.input_provider.UserInputProvider
import kotlinx.coroutines.launch


class LoginAndRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val inputProvider = UserInputProvider(this)
        val firebaseRepo = FirebaseRepository()

        val loginButton = findViewById<Button>(R.id.authButt)
        val registerButton = findViewById<Button>(R.id.registerButton)
        loginButton.setOnClickListener {
            lifecycleScope.launch {
                firebaseRepo.loginUser(this@LoginAndRegisterActivity, inputProvider.getEmail(), inputProvider.getPassword())
            }
        }

        registerButton.setOnClickListener {
            lifecycleScope.launch {
                firebaseRepo.registerUser(this@LoginAndRegisterActivity, inputProvider.getEmail(), inputProvider.getPassword())
            }
        }
    }
}