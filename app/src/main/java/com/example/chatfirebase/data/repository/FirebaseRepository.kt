package com.example.chatfirebase.data.repository
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.chatfirebase.data.model.User
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.ui.profile.ProfileActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class FirebaseRepository {

    private val _database = FirebaseDatabase.getInstance()
    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(context: Context, email: String, password: String) {
        val usersReference = _database.getReference("users")
        val userId = usersReference.push().key
        val userName = email.substringBefore("@")
        val userInfo = User (userId, email, password, userName)
        if (email.contains("@")) {
            usersReference.child(userInfo.userId!!).setValue(userInfo)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.putExtra("email", email)
                        Toast.makeText( context," зарегистрирован успешно", Toast.LENGTH_SHORT).show()
                    } else {
                        println("${task.exception?.message}- ошибка регистрации")
                    }
                }
        }
        else
        {
            Toast.makeText( context," неккоректно введён email", Toast.LENGTH_SHORT).show()
        }

    }

    fun loginUser(context: Context, email: String, password: String) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("email", email)
                    Toast.makeText( context," вход выполнен успешно", Toast.LENGTH_SHORT).show()
                } else {
                    println("${task.exception?.message} - ошибка логина")
                }
            }

    }
    fun sendMessage(context: Context, senderId: String, receiverId: String, message: String) {
        val database = FirebaseDatabase.getInstance()
        val messagesReference = database.getReference("messages")
        val messInfo = UserMessage(senderId, receiverId,message)
        messagesReference.setValue(messInfo).addOnCompleteListener{
                task ->                 if (task.isSuccessful) {
            Toast.makeText( context,"сообщение отправлено успешно", Toast.LENGTH_SHORT).show()
        } else {
            println ("${task.exception?.message} - ошибка отправки смс")
        }
        }
    }

}