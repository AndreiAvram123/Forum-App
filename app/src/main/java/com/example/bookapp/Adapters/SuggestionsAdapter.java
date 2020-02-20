package com.example.bookapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutSuggestionItemBinding;
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
        LayoutSuggestionItemBinding layoutSuggestionItemBinding = LayoutSuggestionItemBinding
                .inflate(layoutInflater, parent, false);
        return new ViewHolder(layoutSuggestionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
        Glide.with(context)
                .load(data.get(position).getPostImage())
                .into(holder.layoutSuggestionItemBinding.suggestionImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSuggestionItemBinding layoutSuggestionItemBinding;

        ViewHolder(@NonNull LayoutSuggestionItemBinding layoutSuggestionItemBinding) {
            super(layoutSuggestionItemBinding.getRoot());
            this.layoutSuggestionItemBinding = layoutSuggestionItemBinding;
        }

        void bindData(Post post) {
            layoutSuggestionItemBinding.setSuggestionPost(post);
        }
    }
}
