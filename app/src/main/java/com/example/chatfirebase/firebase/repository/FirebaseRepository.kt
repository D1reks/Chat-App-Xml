import android.content.Context
import com.example.chatfirebase.data.model.User
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.data.util.UIUtil
import com.example.chatfirebase.firebase.firebase_helper.FirebaseRepositoryHelper
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class FirebaseRepository(private val firebaseHelper: FirebaseRepositoryHelper = FirebaseRepositoryHelper()) {

    suspend fun registerUser(context: Context, email: String, password: String) {
        val usersRef = firebaseHelper.getReference("users")
        val userId = usersRef.push().key
        val userName = email.substringBefore("@")
        val userInfo = User(email, userId, userName, password)

        if (email.contains("@") && userId != null) {
            try {
                firebaseHelper.setValue(usersRef.child(userId), userInfo)
                UIUtil.openProfile(context, userId)
                UIUtil.showToast(context, "Регистрация прошла успешно")
            } catch (e: Exception) {
                println("${e.message} - ошибка регистрации")
            }
        } else {
            println("Некорректный email")
        }
    }

    suspend fun loginUser(context: Context, email: String, password: String) {
        val usersRef = firebaseHelper.getReference("users")
        val query = firebaseHelper.queryByChild(usersRef, "userEmail", email)
        try {
            val snapshot = firebaseHelper.getSingleValue(query)
            if (snapshot.exists()) {
                var userFound = false
                for (child in snapshot.children) {
                    val userInfo = child.getValue(User::class.java)
                    if (userInfo != null) {
                        if (userInfo.userPassword == password) {
                            UIUtil.openProfile(context, userInfo.userId ?: "")
                            UIUtil.showToast(context, "Вход выполнен успешно")
                            userFound = true
                            break
                        }
                    }
                }
                if (!userFound) {
                    UIUtil.showToast(context, "Неверный пароль")
                }
            } else {
                UIUtil.showToast(context, "Пользователь не найден")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UIUtil.showToast(context, "Ошибка базы данных: ${e.message}")
        }
    }

    suspend fun sendMessage(context: Context, senderId: String, receiverId: String, message: String) {
        val messagesRef = firebaseHelper.getReference("messages")
        val messageInfo = UserMessage(senderId, receiverId, message)
        try {
            firebaseHelper.setValue(messagesRef, messageInfo)
            UIUtil.showToast(context, "Сообщение отправлено успешно")
        } catch (e: Exception) {
            println("${e.message} - ошибка отправки сообщения")
        }
    }

    suspend fun loadUserData(userId: String?): Pair<String, String> = suspendCancellableCoroutine { cont ->
        if (userId == null) {
            cont.resumeWithException(Exception("No user id"))
            return@suspendCancellableCoroutine
        }
        val ref = firebaseHelper.getReference("users").child(userId)
        firebaseHelper.addSingleValueListener(ref, { snapshot ->
            try {
                val email = snapshot.child("userEmail").getValue(String::class.java)
                val name = snapshot.child("userName").getValue(String::class.java)
                if (email != null && name != null) {
                    cont.resume(Pair(email, name),  onCancellation = null)
                } else {
                    cont.resumeWithException(Exception("Данные пользователя не найдены"))
                }
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }, { error ->
            cont.resumeWithException(error.toException())
        })
    }
}