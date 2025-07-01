package com.example.chatfirebase.data.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.chatfirebase.data.model.User
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.ui.profile.ProfileActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val database = FirebaseDatabase.getInstance()

    /**
     * Регистрация нового пользователя.
     * Создает запись в базе данных и переходит на профиль.
     */
    suspend fun registerUser(context: Context, email: String, password: String) {
        val usersRef = database.getReference("users")
        val userId = usersRef.push().key
        val userName = email.substringBefore("@")
        val userInfo = User(email, userId, userName, password)

        if (email.contains("@")) {
            userId?.let {
                try {
                    // Сохраняем пользователя в базу
                    usersRef.child(it).setValue(userInfo).await()

                    // Переход на профиль активити
                    val intent = Intent(context, ProfileActivity::class.java).apply {
                        putExtra("email", email)
                    }
                    context.startActivity(intent)

                    // Уведомление об успешной регистрации
                    Toast.makeText(context, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Обработка ошибок
                    println("${e.message} - ошибка регистрации")
                }
            }
        } else {
            println( "Некорректный email")
        }
    }

    /**
     * Вход пользователя.
     * Проверяет email и пароль, переходит на профиль при успешной авторизации.
     */
    suspend fun loginUser(context: Context, email: String, password: String) {
        val usersRef = database.getReference("users")
        val query = usersRef.orderByChild("userEmail").equalTo(email)

        try {
            val snapshot = query.get().await()

            if (snapshot.exists()) {
                var userFound = false
                for (child in snapshot.children) {
                    val userInfo = child.getValue(User::class.java)
                    val userId = userInfo?.userId
                    Toast.makeText(context, "Найден пользователь: ${userInfo?.userEmail}", Toast.LENGTH_SHORT).show()

                    if (userInfo == null) {
                        println("User data is null for key: $userId")
                        continue
                    }

                    println("Found user: ${userInfo.userEmail} with password: ${userInfo.userPassword}")

                    if (userInfo.userPassword == password) {
                        userFound = true
                        // Переход на профиль
                        val intent = Intent(context, ProfileActivity::class.java).apply {
                            putExtra("userId", userId)
                        }
                        context.startActivity(intent)
                        Toast.makeText(context, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                        break
                    }
                }
                if (!userFound) {
                    Toast.makeText(context, "Неверный пароль", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Логируем исключение
            e.printStackTrace()
            Toast.makeText(context, "Ошибка базы данных: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Отправка сообщения между пользователями.
     */
    suspend fun sendMessage(context: Context, senderId: String, receiverId: String, message: String) {
        val messagesRef = database.getReference("messages")
        val messageInfo = UserMessage(senderId, receiverId, message)

        try {
            // Отправляем сообщение и ожидаем завершения операции
            messagesRef.setValue(messageInfo).await()

            // Уведомление об успешной отправке
            Toast.makeText(context, "Сообщение отправлено успешно", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Обработка ошибок
            println("${e.message} - ошибка отправки сообщения")
        }
    }
}