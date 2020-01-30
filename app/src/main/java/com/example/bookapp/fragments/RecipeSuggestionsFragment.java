package com.example.bookapp.fragments;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookapp.Adapters.SuggestionsAdapter;

import java.util.ArrayList;

public class RecipeSuggestionsFragment extends StringDataFragment {

    public static RecipeSuggestionsFragment getInstance(ArrayList<String> suggestions){
        RecipeSuggestionsFragment recipeSuggestionsFragment = new RecipeSuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SUGGESTIONS_ARRAY,suggestions);
        recipeSuggestionsFragment.setArguments(bundle);
        return recipeSuggestionsFragment;
    }

    @Override
    public void setRecyclerViewAdapter() {
        SuggestionsAdapter suggestionsRecyclerView = new SuggestionsAdapter(data,getActivity());
        recyclerView.setAdapter(suggestionsRecyclerView);
    }

    @Override
    public void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }
}
