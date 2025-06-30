package com.example.chatfirebase.data.repository
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {
    fun registerUser(email: String, password: String, completion: (Boolean, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val usersReference = database.getReference("users")
        val userId = usersReference.push().key
        val userInfo = hashMapOf(
            "id" to userId,
            "email" to email,
            "password" to password
        )
        usersReference.child(userId!!).setValue(userInfo).addOnCompleteListener{
                task ->                 if (task.isSuccessful) {
            completion(true, null)
        } else {
            completion(false, task.exception?.message)
        }
        }

    }

    fun loginUser(email: String, password: String, completion: (Boolean, String?) -> Unit) {

    }

    fun logoutUser() {

    }

    fun sendMessage(senderId: String, receiverId: String, message: String, completion: (Boolean, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val messagesReference = database.getReference("messages")
        val messInfo = hashMapOf(
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