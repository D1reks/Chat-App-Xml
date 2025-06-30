package com.example.chatfirebase.input_implementation.input_provider

import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.chatfirebase.R
import com.example.chatfirebase.input_implementation.`interface`.UserInput

class UserInputProvider(private val activity: AppCompatActivity) : UserInput {
    private val emailEditText: EditText = activity.findViewById(R.id.userInputEmail)
    private val passwordEditText: EditText = activity.findViewById(R.id.userInputPassword)

    override fun getEmail(): String = emailEditText.text.toString()
    override fun getPassword(): String = passwordEditText.text.toString()
}