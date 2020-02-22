package com.example.bookapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.databinding.LayoutPostItemBinding;
import com.example.bookapp.fragments.SearchFragment;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {
    private SearchFragment.SearchFragmentInterface searchFragmentInterface;
    private ArrayList<Post> data;
    private Context context;

    public SuggestionsAdapter(ArrayList<Post> data, Activity activity) {
        this.data = data;
        searchFragmentInterface = (SearchFragment.SearchFragmentInterface) activity;
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
                .into(holder.layoutSuggestionItemBinding.suggestionImage);
        holder.layoutSuggestionItemBinding.getRoot().setOnClickListener(view ->
                searchFragmentInterface.fetchSelectedPostById(currentPost.getPostID()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPostItemBinding layoutSuggestionItemBinding;

        ViewHolder(@NonNull LayoutPostItemBinding layoutSuggestionItemBinding) {
            super(layoutSuggestionItemBinding.getRoot());
            this.layoutSuggestionItemBinding = layoutSuggestionItemBinding;
        }

        void bindData(Post post) {
            layoutSuggestionItemBinding.setSuggestionPost(post);
        }
    }
}
