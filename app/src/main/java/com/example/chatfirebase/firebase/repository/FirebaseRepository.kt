import android.content.Context
import com.example.chatfirebase.data.model.ChatUser
import com.example.chatfirebase.data.model.User
import com.example.chatfirebase.data.model.UserMessage
import com.example.chatfirebase.data.util.UIUtil
import com.example.chatfirebase.firebase.firebase_helper.FirebaseRepositoryHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
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

    fun sendMessage(context: Context, userMessage: UserMessage) {
        val messagesRef = firebaseHelper.getReference("messages")
        val messageId = messagesRef.push().key
        if (messageId != null) {
            try {
                messagesRef.child(messageId).setValue(userMessage)
                UIUtil.showToast(context, "Сообщение отправлено успешно")
            } catch (e: Exception) {
                println("${e.message} - ошибка отправки сообщения")
            }
        } else {
            println("ошибка отправки сообщения")
        }
    }

    fun checkUserExistsByEmail(email: String, callback: (Boolean) -> Unit) {
        val databaseRef = firebaseHelper.getReference("users")
        val query = databaseRef.orderByChild("userEmail").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot.exists() && snapshot.childrenCount > 0)
            }
            override fun onCancelled(error: DatabaseError) {
                println("Ошибка при проверке: ${error.message}")
                callback(false)
            }
        })
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
                    cont.resume(Pair(email, name))
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

    suspend fun loadChatList(userId: String?): List<ChatUser> = suspendCancellableCoroutine { cont ->
        if (userId == null) {
            cont.resumeWithException(Exception("No user id"))
            return@suspendCancellableCoroutine
        }
        val ref = firebaseHelper.getReference("messages")
        firebaseHelper.addSingleValueListener(ref, { snapshot ->
            try {
                val usersSet = mutableSetOf<ChatUser>()
                for (child in snapshot.children) {
                    val reciverEmail = child.child("reciverEmail").getValue(String::class.java) ?: ""
                    val reciverName = child.child("reciverName").getValue(String::class.java) ?: ""
                    val senderId = child.child("senderId").getValue(String::class.java) ?: ""
                    val logoChat = "@drawable/chat_list_icon"
                    if (senderId == userId) {
                        val user = ChatUser(reciverName, reciverEmail, logoChat)
                        usersSet.add(user)
                    }
                }
                cont.resume(usersSet.toList())
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }, { error ->
            cont.resumeWithException(error.toException())
        })
    }
}