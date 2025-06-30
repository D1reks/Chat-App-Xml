package com.example.chatfirebase.data.repository
import android.content.Intent
import com.example.chatfirebase.ui.profile.ProfileActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class FirebaseRepository {

    private val _database = FirebaseDatabase.getInstance()
    private val _auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun registerUser(email: String, password: String) {
        val usersReference = _database.getReference("users")
        val userId = usersReference.push().key
        val userInfo = mutableMapOf<String, Any?>(
            "id" to userId,
            "email" to email,
            "password" to password
        )
        usersReference.child(userId!!).setValue(userInfo).addOnCompleteListener{
                task ->                 if (task.isSuccessful) {
            print("Зарегестрирован успешно")
        } else {
            print( task.exception?.message)
        }
        }

    }

    fun loginUser(email: String, password: String) {
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val intent = Intent(this, ProfileActivity::class.java)
                    //intent.putExtra("email", email)
                    ("Вход выполнен успешно")
                } else {
                    print( task.exception?.message)
                }
            }

    }
    fun sendMessage(senderId: String, receiverId: String, message: String, completion: (Boolean, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val messagesReference = database.getReference("messages")
        val messInfo = mutableMapOf<String, Any?>(
            "senderId" to senderId,
            "receiverId" to receiverId,
            "message" to message
        )
        messagesReference.setValue(messInfo).addOnCompleteListener{
                task ->                 if (task.isSuccessful) {
            completion(true, null)
        } else {
            completion(false, task.exception?.message)
        }
        }
    }

}