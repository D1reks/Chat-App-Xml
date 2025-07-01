package com.example.chatfirebase.data.navigation_util

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
}