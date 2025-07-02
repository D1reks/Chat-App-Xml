package com.example.chatfirebase.data.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.chatfirebase.ui.profile.ProfileActivity

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
}