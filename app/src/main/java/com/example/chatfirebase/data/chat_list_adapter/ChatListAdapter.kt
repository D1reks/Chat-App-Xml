

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatfirebase.R
import com.example.chatfirebase.data.model.ChatUser

class ChatListAdapter(
    private val context: Context,
    private val userList: List<ChatUser>
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.userName
        holder.userEmail.text = user.userEmail
        holder.userImage.setImageResource(R.drawable.chat_list_icon)
    }
    override fun getItemCount(): Int = userList.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userEmail: TextView = itemView.findViewById(R.id.userEmail)
    }
    }

