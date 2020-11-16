package com.andrei.kit.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.kit.databinding.RequestItemBinding
import com.andrei.dataLayer.models.deserialization.FriendRequest
import com.andrei.kit.databinding.RequestSentItemBinding
import com.andrei.kit.models.User

class FriendRequestsAdapter(val acceptRequest: ((request: FriendRequest) -> Unit)? = null,
                            private val currentUser : User
)
    : RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder>() {

    abstract inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(friendRequest: FriendRequest)
    }

     enum class ViewType (val id:Int){
         ReceivedRequest(1),
         SentRequest(2)
     }

    private var data: ArrayList<FriendRequest> = ArrayList()


    fun setData(newData: List<FriendRequest>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val request = data[position]
        if(request.receiver.userID == currentUser.userID){
            return ViewType.ReceivedRequest.id
        }
        return ViewType.SentRequest.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(viewType == ViewType.ReceivedRequest.id){
            val binding = RequestItemBinding.inflate(inflater, parent, false)
            return ReceivedFriendRequestsViewHolder(binding)
        }
        val binding = RequestSentItemBinding.inflate(inflater, parent, false)
        return SentFriendRequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendRequestsAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size


    inner class ReceivedFriendRequestsViewHolder(val binding: RequestItemBinding) : ViewHolder(binding.root) {
        override fun bind(friendRequest: FriendRequest) {
            binding.request = friendRequest
                binding.acceptRequestButton.setOnClickListener {
                    acceptRequest?.invoke(friendRequest)
                }

        }
    }
    inner class SentFriendRequestsViewHolder(val binding: RequestSentItemBinding) : ViewHolder(binding.root) {
        override fun bind(friendRequest: FriendRequest) {
            binding.request = friendRequest

        }
    }

}
