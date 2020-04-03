package com.example.bookapp.Adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.R
import com.example.bookapp.databinding.ItemMessageBinding
import com.example.bookapp.databinding.LoadingItemListBinding
import com.example.bookapp.models.UserMessage
import java.util.*

class AdapterMessages(private val adapterInterface: AdapterInterface? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messages: ArrayList<UserMessage> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var currentState: State = State.LOADING
    private val loadingObject: UserMessage = UserMessage.getNullSafeObject()
    private var timeout: Timer = Timer()
    enum class ItemType(val id: Int) {
        TEXT(1), LOADING(0)
    }

    enum class State {
        LOADING, LOADED
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() <= 2
                        && currentState == State.LOADED) {
                    loadMore()
                }
            }
        })
    }

    private fun loadMore() {
        adapterInterface?.let {
            it.requestMoreData(messages.size)
            recyclerView?.post { enableLoading() }

            timeout.schedule(object : TimerTask() {
                override fun run() {
                    recyclerView?.post { disableLoadingItem() }
                }
            }, 3000)
        }

    }

    private fun enableLoading() {
        currentState = State.LOADING
        messages.add(0, loadingObject)
        notifyItemRangeChanged(0, 1)
    }



    fun addMessages(oldMessages: List<UserMessage>) {
        oldMessages.forEach {
            if (!messages.contains(it)) {
                messages.add(0, it)
            }
        }

        notifyItemRangeChanged(0, oldMessages.size)
        disableLoadingItem()


        //this means that we fetched the recent messages
        if (oldMessages.size == messages.size) {
            recyclerView?.smoothScrollToPosition(messages.size - 1)
        }
    }

    fun addNewMessage(newMessage: UserMessage) {
        if (!messages.contains(newMessage)) {
            messages.add(newMessage)
            notifyItemInserted(messages.size - 1)
        }
    }

    private fun disableLoadingItem() {
        currentState = State.LOADED
        if (messages.contains(loadingObject)) {
            messages.remove(loadingObject)
            notifyItemRemoved(0)
        }
        timeout.cancel()
        timeout = Timer()
    }

    fun addNewMessages(newMessages: List<UserMessage>) {
        val oldIndex = messages.size
        newMessages.forEach {
            if (!messages.contains(it)) {
                messages.add(it)
            }
        }
        notifyItemRangeInserted(oldIndex, newMessages.size)
        recyclerView?.smoothScrollToPosition(newMessages.size - 1)
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
        return if (messages[position] == UserMessage.getNullSafeObject()) {
            ItemType.LOADING.id
        } else {
            ItemType.TEXT.id
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    internal inner class MessageViewHolder(private val itemMessageBinding: ItemMessageBinding) : RecyclerView.ViewHolder(itemMessageBinding.root) {
        fun bind(message: UserMessage) {
            itemMessageBinding.message = message
        }

    }

    inner class LoadingViewHolder(binding: LoadingItemListBinding) : RecyclerView.ViewHolder(binding.root)

}