package com.andrei.kit.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.databinding.RequestItemBinding
import com.andrei.dataLayer.models.deserialization.FriendRequest

class FriendRequestsAdapter(val acceptRequest: (request: FriendRequest) -> Unit)
    : RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder>() {

    private var data: ArrayList<FriendRequest> = ArrayList()


    fun setData(newData: List<FriendRequest>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RequestItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val binding: RequestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FriendRequest) {
            binding.request = item
            binding.acceptRequestButton.setOnClickListener {
                acceptRequest(item)
            }

        }
    }

}
