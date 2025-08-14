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

    private val _firebaseRepo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.authButt)
        val registerButton = findViewById<Button>(R.id.registerButton)

        val inputProvider = UserInputProvider(this)

        loginButton.setOnClickListener {
            lifecycleScope.launch {
                _firebaseRepo.loginUser(this@LoginAndRegisterActivity, inputProvider.getEmail(), inputProvider.getPassword())
            }
        }

        registerButton.setOnClickListener {
            lifecycleScope.launch {
                _firebaseRepo.registerUser(this@LoginAndRegisterActivity, inputProvider.getEmail(), inputProvider.getPassword())
            }
        }
    }
}