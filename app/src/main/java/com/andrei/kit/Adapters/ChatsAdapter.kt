package com.andrei.kit.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.databinding.ItemChatBinding
import com.andrei.kit.fragments.SocialFragmentDirections
import com.andrei.kit.models.Chat

class ChatsAdapter : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    private var chats: ArrayList<Chat> = ArrayList()

    fun setData(data: List<Chat>) {
        chats.clear()
        chats.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatBinding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }


    inner class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(chat: Chat) {
            binding.chat = chat

            binding.root.setOnClickListener {
                //remove notification,as the user clicked on the chat
                chats.find { it.chatID == chat.chatID }.also {
                    if (it != null) {
                        it.newMessage = false
                        notifyItemChanged(chats.indexOf(it))
                    }
                }
                val action: NavDirections = SocialFragmentDirections.actionGlobalMessagesFragment(chat.chatID)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

    }


}