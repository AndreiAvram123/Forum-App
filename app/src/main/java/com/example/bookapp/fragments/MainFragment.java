package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private static final String  KEY_POPULAR_RECIPES = "KEY_POPULAR_BOOKS";
    private RecyclerView recyclerView;
    private ArrayList<Recipe> popularRecipes;

    public static MainFragment getInstance(@NonNull ArrayList<Recipe> popularRecipes){
        MainFragment randomRacipesFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_POPULAR_RECIPES, popularRecipes);
        randomRacipesFragment.setArguments(bundle);
        return randomRacipesFragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_books, container, false);
        this.popularRecipes = getArguments().getParcelableArrayList(KEY_POPULAR_RECIPES);
        recyclerView = view.findViewById(R.id.recycler_view_popular_books);
        initializeRecyclerViewAdapter();
        return view;
    }

    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void  initializeRecyclerViewAdapter(){
        recyclerView.setAdapter(new AdapterRecyclerView(popularRecipes));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }


}
