package com.example.bookapp.Adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.ItemMessageBinding
import com.example.bookapp.databinding.LoadingItemListBinding
import com.example.bookapp.models.Message
import java.util.*

class AdapterMessages : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: ArrayList<Message> = ArrayList()
    private var isLoading = false

    enum class ItemType(val id: Int) {
        TEXT(1), LOADING(0)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null && !isLoading && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    isLoading = true
                }
            }
        })
    }


    fun addOldMessages(oldMessages: List<Message>) {
        oldMessages.forEach{
            if(!messages.contains(it)){
                messages.add(it)
            }
        }
        isLoading = false
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemType.TEXT.id -> MessageViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_message, parent, false))
            else -> LoadingViewHolder(DataBindingUtil.inflate(inflater, R.layout.loading_item_list, parent, false))

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageViewHolder) {
            holder.bind(messages[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position] == Message.getNullSafeObject()) {

            ItemType.LOADING.id
        } else {
            ItemType.TEXT.id
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    internal inner class MessageViewHolder(private val itemMessageBinding: ItemMessageBinding) : RecyclerView.ViewHolder(itemMessageBinding.root) {
        fun bind(message: Message?) {
            itemMessageBinding.message = message
        }

    }

    inner class LoadingViewHolder(binding: LoadingItemListBinding) : RecyclerView.ViewHolder(binding.root)

}