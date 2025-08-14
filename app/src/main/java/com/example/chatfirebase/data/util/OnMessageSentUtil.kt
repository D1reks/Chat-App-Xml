package com.example.chatfirebase.data.util

import ChatListAdapter
import FirebaseRepository
import com.example.chatfirebase.data.model.ChatUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OnMessageSentUtil {

    fun onMessageSent(
        userId: String?,
        firebaseRepo: FirebaseRepository,
        users: MutableList<ChatUser>,
        adapter: ChatListAdapter
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val loadedUsers = firebaseRepo.loadChatList(userId)
                users.clear()
                users.addAll(loadedUsers)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}