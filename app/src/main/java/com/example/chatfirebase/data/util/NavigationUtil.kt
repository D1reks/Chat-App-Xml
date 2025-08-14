package com.example.chatfirebase.data.util

import android.app.Activity
import android.content.Context
import android.content.Intent

object NavigationUtil {
    fun openNextIntent(context: Context, targetActivityClass: Class<out Activity>, userId: String?) {
        val intent = Intent(context, targetActivityClass).apply {
            putExtra("userId", userId)
        }
        // Если context не является Activity, добавляем флаг
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun openChatIntent(context: Context, targetActivityClass: Class<out Activity>, userId: String?, reciverEmail: String, reciverName: String) {
        val intent = Intent(context, targetActivityClass).apply {
            putExtra("userId", userId)
            putExtra("reciverEmail", reciverEmail)
            putExtra("reciverName", reciverName)
        }
        // Если context не является Activity, добавляем флаг
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}