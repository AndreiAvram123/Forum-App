package com.example.bookapp.fragments;



import android.os.Bundle;

import androidx.annotation.Nullable;
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


public class SearchSuggestionsFragment extends Fragment {
private static final String KEY_SUGGESTIONS_ARRAY ="KEY_SUGGESTIONS_ARRAY";
private ArrayList<String> suggestions;
private SuggestionsRecyclerView suggestionsRecyclerView;


    public static SearchSuggestionsFragment getInstance(ArrayList<String>suggestions){
        SearchSuggestionsFragment oldSearchSuggestionsFragment = new SearchSuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SUGGESTIONS_ARRAY,suggestions);
        oldSearchSuggestionsFragment.setArguments(bundle);
        return oldSearchSuggestionsFragment;
    }

    public SearchSuggestionsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_old_searches, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_past_searches);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(suggestionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        suggestions = getArguments().getStringArrayList(KEY_SUGGESTIONS_ARRAY);
        if(suggestions == null){
            suggestions = new ArrayList<>();
        }
        suggestionsRecyclerView = new SuggestionsRecyclerView(suggestions,getActivity());
    }

}
