package com.example.bookapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.bookapp.R;
import com.example.bookapp.fragments.SearchFragment;

import java.util.ArrayList;

public class SuggestionsAdapter extends AdapterStrings {
    private SearchFragment.SearchFragmentInterface searchFragmentInterface;

    public SuggestionsAdapter(ArrayList<String> data, Activity activity) {
        super(data);
        searchFragmentInterface = (SearchFragment.SearchFragmentInterface) activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.text = layout.findViewById(R.id.suggestion_item_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(view -> searchFragmentInterface.performSearch(data.get(position)));
    }
}
