package com.andrew.bookapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrew.bookapp.databinding.UserSuggestionBinding
import com.andrew.bookapp.models.User
import java.util.*

class SuggestionsAdapter(val callback: Callback) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
    private var context: Context? = null
    var data: ArrayList<User> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutSuggestionItemBinding = UserSuggestionBinding
                .inflate(layoutInflater, parent, false)
        return ViewHolder(layoutSuggestionItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = data[position]
        holder.bindData(currentPost)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(var binding: UserSuggestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: User) {
            binding.user = user
            binding.addFriendButton.setOnClickListener {
                callback.sendFriendRequest(user)
                binding.addFriendButton.visibility = View.INVISIBLE
            }
        }

    }

    interface Callback {
        fun sendFriendRequest(receiver: User)
    }

}