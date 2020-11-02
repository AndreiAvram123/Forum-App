package com.andrei.kit.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.databinding.MessageImageReceivedBinding
import com.andrei.kit.databinding.MessageImageSentBinding
import com.andrei.kit.databinding.MessageReceivedBinding
import com.andrei.kit.databinding.MessageSentBinding
import com.andrei.kit.models.Message
import com.andrei.kit.models.User
import com.andrei.dataLayer.repositories.OperationStatus
import com.andrei.dataLayer.serverConstants.MessageTypes
import com.andrei.kit.getScreenWidth


class MessageAdapter(private val currentUser: User,
                     val expand: (imageURL: String, local: Boolean) -> Unit) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val messages: ArrayList<Message> = ArrayList()

    private var adapterRecyclerView: RecyclerView? = null


    internal enum class ViewTypes(val id: Int) {
        LOADING(0), MESSAGE_RECEIVED_TEXT(1), MESSAGE_SENT_TEXT(2), MESSAGE_SENT_IMAGE(3), MESSAGE_RECEIVED_IMAGE(4),
    }

    inner class MessageReceivedViewHolder(val binding: MessageReceivedBinding) : ViewHolder(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
        }
    }

    inner class MessageSentViewHolder(val binding: MessageSentBinding) : ViewHolder(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
        }
    }

    inner class MessageImageSentViewHolder(val binding: MessageImageSentBinding) : ViewHolder(binding.root) {

        override fun bind(message: Message) {
            binding.message = message
            binding.halfScreenWidth = binding.root.context.getScreenWidth()/2
            binding.messageImage.setOnClickListener {
                expandImage(message)
            }
        }
    }

    inner class MessageImageReceivedViewHolder(val binding: MessageImageReceivedBinding) : ViewHolder(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
            binding.halfScreenWidth = binding.root.context.getScreenWidth()

            binding.messageImage.setOnClickListener {
                expandImage(message)
            }
        }
    }

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(message: Message)
    }

    fun expandImage(message: Message) {
        expand(message.content, false)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        val inflator: LayoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ViewTypes.MESSAGE_RECEIVED_TEXT.id -> {
                val binding = MessageReceivedBinding.inflate(inflator, parent, false)
                return MessageReceivedViewHolder(binding)
            }
            ViewTypes.MESSAGE_SENT_TEXT.id -> {
                val binding = MessageSentBinding.inflate(inflator, parent, false)
                return MessageSentViewHolder(binding)
            }
            ViewTypes.MESSAGE_SENT_IMAGE.id -> {
                val binding = MessageImageSentBinding.inflate(inflator, parent, false)
                return MessageImageSentViewHolder(binding)
            }
            ViewTypes.MESSAGE_RECEIVED_IMAGE.id -> {
                val binding = MessageImageReceivedBinding.inflate(inflator, parent, false)
                return MessageImageReceivedViewHolder(binding)
            }
            else -> {
                val binding = MessageImageReceivedBinding.inflate(inflator, parent, false)
                return MessageImageReceivedViewHolder(binding)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapterRecyclerView = recyclerView

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(viewHolder: MessageAdapter.ViewHolder, position: Int) = viewHolder.bind(messages[position])


    fun add(message: Message) {
                messages.add(message)
                notifyItemInserted(messages.size - 1)
                scrollToLast()
    }

    private fun scrollToLast() {
        adapterRecyclerView?.scrollToPosition(messages.size - 1)
    }


    fun setData(newMessages: List<Message>) {
        newMessages.forEach {
            if (!messages.contains(it)) {
                add(it)
            }
        }
    }

    fun clear() {
        messages.clear()
    }


    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        when (message.type) {
            MessageTypes.imageMessageType -> {
                if (message.sender.userID == currentUser.userID) {
                    return ViewTypes.MESSAGE_SENT_IMAGE.id
                } else {
                    return ViewTypes.MESSAGE_RECEIVED_IMAGE.id
                }
            }
            MessageTypes.textMessage -> {
                if (message.sender.userID == currentUser.userID) {
                    return ViewTypes.MESSAGE_SENT_TEXT.id
                } else {
                    return ViewTypes.MESSAGE_RECEIVED_TEXT.id
                }
            }
        }
        return ViewTypes.LOADING.id

    }


}