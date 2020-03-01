package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;
import com.example.bookapp.databinding.ItemMessageBinding;
import com.example.bookapp.models.Message;

import java.util.ArrayList;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolder> {

    private ArrayList<Message> messages;

    public AdapterMessages(@Nullable ArrayList<Message> messages) {
        if (messages == null) {
            messages = new ArrayList<Message>();
        }
        this.messages = messages;
    }

    public void setData(@NonNull ArrayList<Message> data) {
        this.messages = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding itemMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message, parent, false);
        return new ViewHolder(itemMessageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(messages.get(position));
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
}
