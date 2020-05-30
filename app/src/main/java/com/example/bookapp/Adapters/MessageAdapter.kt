package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.MessageReceivedBinding
import com.example.bookapp.databinding.MessageSentBinding
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User

class MessageAdapter(private val currentUser: User) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: ArrayList<MessageDTO> = ArrayList()

    internal enum class ViewTypes(val id: Int) {
        LOADING(0), MESSAGE_RECEIVED(1), MESSAGE_SENT(2)
    }

    inner class MessageReceivedViewHolder(val binding: MessageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDTO: MessageDTO) {
            binding.message = messageDTO
        }
    }

    inner class MessageSentViewHolder(val binding: MessageSentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDTO: MessageDTO) {
            binding.message = messageDTO
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflator: LayoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ViewTypes.MESSAGE_RECEIVED.id -> {
                val binding = MessageReceivedBinding.inflate(inflator, parent, false)
                return MessageReceivedViewHolder(binding)
            }
            ViewTypes.MESSAGE_SENT.id -> {
                val binding = MessageSentBinding.inflate(inflator, parent, false)
                return MessageSentViewHolder(binding)
            }
            else -> {
                //todo
                //modify
                val binding = MessageSentBinding.inflate(inflator, parent, false)
                return MessageSentViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is MessageSentViewHolder) {
            viewHolder.bind(messages[position])
        }
        if (viewHolder is MessageReceivedViewHolder) {
            viewHolder.bind(messages[position])
        }
    }

    fun add(message: MessageDTO) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        return if (message.sender.userID == currentUser.userID) {
            ViewTypes.MESSAGE_SENT.id
        } else {
            ViewTypes.MESSAGE_RECEIVED.id
        }

    }
}