package com.example.bookapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.example.bookapp.R;
import com.example.bookapp.databinding.ItemMessageBinding;
import com.example.bookapp.databinding.LoadingItemListBinding;
import com.example.bookapp.interfaces.MessageInterface;
import com.example.bookapp.models.Message;

import java.util.ArrayList;

public class AdapterMessages extends RecyclerView.Adapter {

    private ArrayList<Message> messages;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    private boolean shouldScrollFirstTime = true;
    private MessageInterface callback;
    private String user2ID;
    private final int VIEW_TYPE_ITEM = 1;
    private int VIEW_TYPE_LOADING = 0;

    public AdapterMessages(@NonNull RecyclerView recyclerView, @NonNull String user2ID) {
        this.messages = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.user2ID = user2ID;
        attachScrollListener();

    }

    public void setCallback(MessageInterface callback) {
        this.callback = callback;
    }


    private void attachScrollListener() {
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && !isLoading && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    isLoading = true;
                    Log.d("Debug", "trying to load more .....");
                    //todo

//                    if (callback != null) {
//                        callback.fetchMoreMessages(user2ID, messages.size());
//                    }
                }
            }
        });
    }


    public void addOldMessages(@NonNull ArrayList<Message> oldMessages) {
        isLoading = true;
        for (Message message : oldMessages) {
            if (!this.messages.contains(message)) {
                this.messages.add(0, message);
            }
        }
        notifyItemRangeChanged(0, oldMessages.size());
        //maintain current position
        scroll(oldMessages.size() - 1);
        isLoading = false;
    }

    public void addMessage(@NonNull Message lastFetchedMessage) {
        messages.add(lastFetchedMessage);
        notifyItemInserted(messages.size() - 1);
    }

    public void addNewMessages(@NonNull ArrayList<Message> newFetchedMessages) {
        int start = messages.size() - 1;
        messages.addAll(newFetchedMessages);
        notifyItemRangeInserted(start, newFetchedMessages.size());
    }


    private void scroll(int position) {
        if (recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().scrollToPosition(position);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            ItemMessageBinding itemMessageBinding = DataBindingUtil.inflate(inflater, R.layout.item_message, parent, false);
            return new ViewHolder(itemMessageBinding);
        } else {
            LoadingItemListBinding binding = DataBindingUtil.inflate(inflater, R.layout.loading_item_list, parent, false);
            return new LoadingViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bind(messages.get(position));
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (messages.get(position) != null)
            return VIEW_TYPE_ITEM;
        else
            return VIEW_TYPE_LOADING;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMessageBinding itemMessageBinding;
        ViewHolder(@NonNull ItemMessageBinding itemMessageBinding) {
            super(itemMessageBinding.getRoot());
            this.itemMessageBinding = itemMessageBinding;
        }

        void bind(Message message) {
           itemMessageBinding.setMessage(message);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {


        LoadingViewHolder(@NonNull LoadingItemListBinding binding) {
            super(binding.getRoot());
        }
    }

}
