package com.andrei.kit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.kit.databinding.UserSuggestionBinding
import com.andrei.kit.fragments.SearchFragmentDirections
import com.andrei.kit.models.User
import java.util.*

class SuggestionsAdapter : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
    private var context: Context? = null
    var data: MutableList<User> = ArrayList()
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
            binding.root.setOnClickListener {
                val action = SearchFragmentDirections.actionGlobalProfileFragment(user.userID)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

    }


}