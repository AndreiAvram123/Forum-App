package com.example.bookapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.AdapterRecyclerView;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class DataFragment extends Fragment {

    private static final String KEY_DATA = "KEY_DATA";
    private RecyclerView recyclerView;
    private ArrayList<Recipe> data;

    public static DataFragment getInstance(@NonNull ArrayList<Recipe> recipes) {
        DataFragment dataFragment = new DataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_DATA, recipes);
        dataFragment.setArguments(bundle);
        return dataFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        if (data != null) {
            view = inflater.inflate(R.layout.fragment_popular_books, container, false);
            recyclerView = view.findViewById(R.id.recycler_view_popular_books);
            initializeRecyclerViewAdapter();

        } else {
            view = inflater.inflate(R.layout.layout_fragment_error_message, container, false);
            TextView errorMessage = view.findViewById(R.id.error_message_fragment_error);
            errorMessage.setText("You do not have any data");
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null && getArguments().getParcelableArrayList(KEY_DATA).size() != 0) {
            // Inflate the layout for this fragment
            this.data = getArguments().getParcelableArrayList(KEY_DATA);
        }
    }

    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void initializeRecyclerViewAdapter() {
        recyclerView.setAdapter(new AdapterRecyclerView(data, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

}
