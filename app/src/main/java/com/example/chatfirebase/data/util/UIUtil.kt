package com.example.chatfirebase.data.util

import ChatListAdapter
import FirebaseRepository
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.chatfirebase.R
import com.example.chatfirebase.data.model.ChatUser
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.ui.profile.ProfileActivity
import kotlinx.coroutines.launch

object UIUtil {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    //upd. потом надо переделать, чтобы использовать не только для конкретной Activity
    fun openProfile(context: Context, userId: String) {
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    fun updateTextViewText(context: Context, viewId: Int, newText: String?) {
        val textView = (context as? Activity)?.findViewById<TextView>(viewId)
        textView?.text = newText
    }

    fun showCustomDialog(context: Context, senderId: String?, lifecycleOwner: LifecycleOwner, _users:MutableList<ChatUser>, _adapter: ChatListAdapter) {
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_message, null)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextMessage = dialogView.findViewById<EditText>(R.id.editTextMessage)

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Send") { _, _ ->
                val userMessage = UserMessage(
                    senderId,
                    editTextEmail.text.toString(),
                    editTextEmail.text.toString().substringBefore("@"),
                    editTextMessage.text.toString()
                )
                val firebaserepo = FirebaseRepository()
                firebaserepo.checkUserExistsByEmail(userMessage.reciverEmail) { exists ->
                    if (exists) {

                        lifecycleOwner.lifecycleScope.launch {
                            try {
                                firebaserepo.sendMessage(context, userMessage)
                                OnMessageSentUtil.onMessageSent(senderId, firebaserepo, _users, _adapter)
                            } catch (e: Exception) {
                                // Обработка ошибок
                                println( e.message)
                            }
                        }
                    } else {
                        showToast(context, "Пользователь с таким email не найден.")
                    }
                }
                // Запуск корутины внутри диалог
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}