package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.Adapters.AdapterRecyclerViewIngredients;
import com.example.bookapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIngredients extends StringDataFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.layout_fragment_ingredients, container, false);
        recyclerView = layout.findViewById(R.id.recycler_view_recycle_fragment);
        return layout;
    }

    @Override
    public void initializeRecyclerViewAdapter() {

        recyclerView.setAdapter(new AdapterRecyclerViewIngredients(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollHorizontally() {
                return false ;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }
}
