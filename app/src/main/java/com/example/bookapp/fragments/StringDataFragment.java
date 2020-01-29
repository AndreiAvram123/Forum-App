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


public class StringDataFragment extends Fragment {
private static final String KEY_SUGGESTIONS_ARRAY ="KEY_SUGGESTIONS_ARRAY";
ArrayList<String> data;
RecyclerView recyclerView;


    public static StringDataFragment getInstance(ArrayList<String>suggestions){
        StringDataFragment oldStringDataFragment = new StringDataFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SUGGESTIONS_ARRAY,suggestions);
        oldStringDataFragment.setArguments(bundle);
        return oldStringDataFragment;
    }

    public StringDataFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_fragment_string_data, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_past_searches);
        initializeRecyclerViewAdapter();
        return view;
    }

    public void initializeRecyclerViewAdapter(){
        SuggestionsRecyclerView suggestionsRecyclerView = new SuggestionsRecyclerView(data, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(suggestionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getArguments().getStringArrayList(KEY_SUGGESTIONS_ARRAY);
        if(data == null){
            data = new ArrayList<>();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initializeRecyclerViewAdapter();
    }
}
