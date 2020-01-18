package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

//todo
//maybe reuse this fragment

/**
 * This fragment is used to
 * display a list of search results
 */
public class SearchResultsFragment extends Fragment {
private static final String  SEARCH_RESULTS_KEY = "SEARCH_RESULTS_KEY";
private RecyclerView resultsRecyclerView;

    public SearchResultsFragment getInstance(ArrayList<Recipe> searchResults) {
        // Required empty public constructor
        SearchResultsFragment searchHistoryFragment = new SearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SEARCH_RESULTS_KEY,searchResults);
        searchHistoryFragment.setArguments(bundle);
        return searchHistoryFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_search_results, container, false);
        resultsRecyclerView = layout.findViewById(R.id.recycler_view_search_results);
        return layout;
    }
    private void initializeRecyclerView(){

    }


}
