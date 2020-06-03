package com.example.bookapp.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.RequestItemBinding
import com.example.dataLayer.models.deserialization.DeserializeFriendRequest

class FriendRequestsAdapter(val acceptRequest: (request: DeserializeFriendRequest) -> Unit)
    : RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder>() {

    private var data: ArrayList<DeserializeFriendRequest> = ArrayList()


    fun setData(newData: List<DeserializeFriendRequest>) {
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
        fun bind(item: DeserializeFriendRequest) {
            binding.request = item
            binding.acceptRequestButton.setOnClickListener {
                acceptRequest(item)
                data.remove(item)
                notifyItemRemoved(data.indexOf(item))
            }

        }
    }

}
