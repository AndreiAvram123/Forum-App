package com.example.bookapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.ItemFriendsBinding
import com.example.bookapp.fragments.ChatsFragmentDirections
import com.example.bookapp.models.User

class FriendsAdapter(val navController: NavController) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    private var friends: ArrayList<User> = ArrayList()

    fun setData(friends: ArrayList<User>) {
        this.friends = friends
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFriendsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_friends, parent, false)
        //ViewHolder viewHolder = new View
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(friends[position])
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    inner class ViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(friend: User?) {
            binding.friend = friend
            binding.root.setOnClickListener {
                val action: NavDirections = ChatsFragmentDirections.actionFriendsFragmentToMessagesFragment(1)
                navController.navigate(action)
            }
        }

    }


}