package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.MessageImageReceivedBinding
import com.example.bookapp.databinding.MessageImageSentBinding
import com.example.bookapp.databinding.MessageReceivedBinding
import com.example.bookapp.databinding.MessageSentBinding
import com.example.bookapp.fragments.MessagesFragmentDirections
import com.example.bookapp.models.LocalImageMessage
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.dataLayer.repositories.UploadProgress
import com.example.dataLayer.serverConstants.MessageTypes

class MessageAdapter(private val currentUser: User, val expandImage: (imageURL: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: ArrayList<MessageDTO> = ArrayList()
    private var adapterRecyclerView: RecyclerView? = null

    internal enum class ViewTypes(val id: Int) {
        LOADING(0), MESSAGE_RECEIVED_TEXT(1), MESSAGE_SENT_TEXT(2), MESSAGE_SENT_IMAGE(3), MESSAGE_RECEIVED_IMAGE(4),
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


    inner class MessageImageSentViewHolder(val binding: MessageImageSentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDTO: MessageDTO) {
            binding.message = messageDTO
            if (messageDTO is LocalImageMessage) {
                binding.messageImage.setImageDrawable(messageDTO.drawable)
                if (messageDTO.currentStatus == UploadProgress.UPLOADING) {
                    binding.messageImage.alpha = 0.5f
                }
            }

            binding.messageImage.setOnClickListener {
                expandImage(messageDTO.content)
            }
        }
    }

    inner class MessageImageReceivedViewHolder(val binding: MessageImageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDTO: MessageDTO) {
            binding.message = messageDTO
            binding.messageImage.setOnClickListener {
                expandImage(messageDTO.content)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
                //todo
                //modify
                val binding = MessageSentBinding.inflate(inflator, parent, false)
                return MessageSentViewHolder(binding)
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

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is MessageSentViewHolder) {
            viewHolder.bind(messages[position])
        }
        if (viewHolder is MessageReceivedViewHolder) {
            viewHolder.bind(messages[position])
        }
        if (viewHolder is MessageImageSentViewHolder) {
            viewHolder.bind(messages[position])
        }
        if (viewHolder is MessageImageReceivedViewHolder) {
            viewHolder.bind(messages[position])
        }
    }

    fun add(message: MessageDTO) {
        messages.find { it is LocalImageMessage && it.localID == message.localID }.also {
            if (it == null) {
                messages.add(message)
                notifyItemInserted(messages.size - 1)
                scrollToLast()
            } else {
                if (it is LocalImageMessage) {
                    it.currentStatus = UploadProgress.UPLOADED
                    notifyItemChanged(messages.indexOf(it))
                }
            }
        }

    }

    private fun scrollToLast() {
        adapterRecyclerView?.scrollToPosition(messages.size - 1)
    }

    fun addNewMessages(newMessages: List<MessageDTO>) {
        val oldIndex = messages.size - 1
        messages.addAll(newMessages)
        notifyItemRangeInserted(oldIndex, messages.size)
        scrollToLast()
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