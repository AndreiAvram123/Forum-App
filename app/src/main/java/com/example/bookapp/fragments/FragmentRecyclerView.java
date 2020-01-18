package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.Adapters.AdapterRecyclerViewIngredients;
import com.example.bookapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecyclerView extends Fragment {

private RecyclerView recyclerView;
private ArrayList<String> items;
private static final String KEY_ITEMS_LIST = "KEY_ITEMS_LIST";

  public static FragmentRecyclerView getInstance(ArrayList<String> items){
      FragmentRecyclerView fragmentRecyclerView = new FragmentRecyclerView();
      Bundle bundle = new Bundle();
      bundle.putStringArrayList(KEY_ITEMS_LIST,items);
      fragmentRecyclerView.setArguments(bundle);
      return fragmentRecyclerView;

  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_fragment_recycler_view, container, false);
        recyclerView = layout.findViewById(R.id.recycler_view_recycle_fragment);
        items = getArguments().getStringArrayList(KEY_ITEMS_LIST);
        initializeRecyclerViewAdapter();
        return layout;
    }
    private void  initializeRecyclerViewAdapter(){
        recyclerView.setAdapter(new AdapterRecyclerViewIngredients(items));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }
}
