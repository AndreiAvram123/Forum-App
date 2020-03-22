package com.example.bookapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.databinding.LayoutPostItemBinding;
import com.example.bookapp.fragments.ExpandedItemFragmentDirections;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {
    private ArrayList<Post> data;
    private Context context;

    public SuggestionsAdapter(ArrayList<Post> data) {
        this.data = data;
    }

    public ArrayList<Post> getData() {
        return data;
    }
    public void setData(ArrayList<Post> data){
        this.data = data;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutPostItemBinding layoutSuggestionItemBinding = LayoutPostItemBinding
                .inflate(layoutInflater, parent, false);
        return new ViewHolder(layoutSuggestionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post currentPost = data.get(position);
        holder.bindData(currentPost);
        Glide.with(context)
                .load(currentPost.getPostImage())
                .into(holder.binding.suggestionImage);
        holder.binding.getRoot().setOnClickListener(view -> {
            NavDirections action = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(currentPost.getPostID());
            Navigation.findNavController(holder.binding.getRoot()).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPostItemBinding binding;

        ViewHolder(@NonNull LayoutPostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(Post post) {
            binding.setSuggestionPost(post);
        }
    }
}
