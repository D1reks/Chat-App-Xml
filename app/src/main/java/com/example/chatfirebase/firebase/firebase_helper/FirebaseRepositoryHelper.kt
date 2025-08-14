package com.example.chatfirebase.firebase.firebase_helper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryHelper(private val database: FirebaseDatabase = FirebaseDatabase.getInstance()) {

    fun getReference(path: String) = database.getReference(path)

    suspend fun setValue(ref: DatabaseReference, value: Any) {
        ref.setValue(value).await()
    }

    suspend fun getSingleValue(query: Query): DataSnapshot = query.get().await()

    fun queryByChild(ref: DatabaseReference, child: String, value: String) = ref.orderByChild(child).equalTo(value)

    fun addSingleValueListener(ref: DatabaseReference, onDataChange: (DataSnapshot) -> Unit, onCancelled: (DatabaseError) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) = onDataChange(snapshot)
            override fun onCancelled(error: DatabaseError) = onCancelled(error)
        })
    }
}
