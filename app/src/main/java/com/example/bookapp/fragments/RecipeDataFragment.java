package com.example.bookapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.AdapterRecipesData;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class RecipeDataFragment extends Fragment {

    private static final String KEY_DATA = "KEY_DATA";
    private RecyclerView recyclerView;
    private ArrayList<Recipe> data;
    private TextView sortTextView;
    private Spinner sortOptionsSpinner;
    private AdapterRecipesData adapterRecipesData;
    private TextView numberResults;

    public static RecipeDataFragment getInstance(@NonNull ArrayList<Recipe> recipes) {
        RecipeDataFragment recipeDataFragment = new RecipeDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_DATA, recipes);
        recipeDataFragment.setArguments(bundle);
        return recipeDataFragment;
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
     * Lifecycle method called when
     * the view is created
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        if (data != null) {
            view = inflater.inflate(R.layout.layout_fragment_recipe_data, container, false);
            initializeViews(view);
            initializeRecyclerViewAdapter();
            bindDataToViews();
        }
        return view;
    }

    private void bindDataToViews() {
        numberResults.setText(Integer.toString(data.size()));
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_parameters,
                R.layout.custom_item_spinner);
        sortOptionsSpinner.setAdapter(spinnerAdapter);
        sortTextView.setOnClickListener(view -> sortOptionsSpinner.performClick());
        //make a final boolean array in order to access it from
        //withing the inner class
        final boolean[] notFirstCall = {false};
        sortOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 //the onItemSelected method is called the first time when the listener is attached
                if(notFirstCall[0]) {
                    String sortCriteria = parent.getItemAtPosition(position).toString();
                    adapterRecipesData.sort(sortCriteria);
                }
                notFirstCall[0] = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_popular_books);
        sortTextView = view.findViewById(R.id.sort_text);
        sortOptionsSpinner = view.findViewById(R.id.spinner_search_options);
        numberResults = view.findViewById(R.id.text_number_results);
    }


    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void initializeRecyclerViewAdapter() {
        adapterRecipesData = new AdapterRecipesData(data, getActivity());
        recyclerView.setAdapter(adapterRecipesData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

}
