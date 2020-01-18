package com.example.bookapp.fragments;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.Adapters.SuggestionsRecyclerView;
import com.example.bookapp.R;

import java.util.ArrayList;


public class SearchHistoryFragment extends Fragment {
private static final String KEY_SUGGESTIONS_ARRAY ="KEY_SUGGESTIONS_ARRAY";

    //the default suggestions given are the old ones
    //that the user entered
    public static SearchHistoryFragment getInstance(ArrayList<String>suggestions){
        SearchHistoryFragment oldSearchSuggestionsFragment = new SearchHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SUGGESTIONS_ARRAY,suggestions);
        oldSearchSuggestionsFragment.setArguments(bundle);
        return oldSearchSuggestionsFragment;
    }

    public SearchHistoryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_old_searches, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_past_searches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new SuggestionsRecyclerView(getArguments().getStringArrayList(KEY_SUGGESTIONS_ARRAY)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return view;
    }


}
