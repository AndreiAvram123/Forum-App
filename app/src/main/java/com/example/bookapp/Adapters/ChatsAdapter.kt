package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.ItemFriendsBinding
import com.example.bookapp.fragments.FriendsFragmentDirections
import com.example.bookapp.models.Chat

class ChatsAdapter() : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    private var chats: ArrayList<Chat> = ArrayList()

    fun setData(chats: ArrayList<Chat>) {
        this.chats = chats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFriendsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_friends, parent, false)
        //ViewHolder viewHolder = new View
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    inner class ViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(chat: Chat) {
            binding.chat = chat

            binding.root.setOnClickListener {
                val action: NavDirections = FriendsFragmentDirections.actionFriendsToMessagesFragment(chat.chatID)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

    }


}