package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.MessageReceivedBinding
import com.example.bookapp.models.MessageDTO

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private val messages: ArrayList<MessageDTO> = ArrayList()


    inner class ViewHolder(val binding: MessageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageDTO: MessageDTO) {
            binding.message = messageDTO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageReceivedBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    fun add(message: MessageDTO) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}