package com.example.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.RecyclerViewAdapterPosts;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentPostsDataBinding;
import com.example.bookapp.models.ViewModelPost;

public class FavoritePostsFragment extends Fragment {

    private RecyclerViewAdapterPosts recyclerViewAdapterPosts;
    private LayoutFragmentPostsDataBinding binding;
    private ViewModelPost viewModelPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (viewModelPost == null) {
            attachObserver();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_posts_data, container, false);
        initializeRecyclerViewAdapter();
        bindDataToViews();
        return binding.getRoot();
    }

    private void attachObserver() {
        viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelPost.getSavedPosts().observe(getViewLifecycleOwner(), savedPosts -> {
            recyclerViewAdapterPosts.setData(savedPosts);
        });
    }


    private void bindDataToViews() {
        binding.numberResults.setText(viewModelPost.getSavedPosts().getValue().size() + "");
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_parameters,
                R.layout.custom_item_spinner);
        binding.spinnerSortOptions.setAdapter(spinnerAdapter);
        binding.sortTextButton.setOnClickListener(view -> binding.spinnerSortOptions.performClick());
        //make a final boolean array in order to access it from
        //withing the inner class
        final boolean[] notFirstCall = {false};
        binding.spinnerSortOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //the onItemSelected method is called the first time when the listener is attached
                if (notFirstCall[0]) {
                    String sortCriteria = parent.getItemAtPosition(position).toString();
                    recyclerViewAdapterPosts.sort(sortCriteria);
                }
                notFirstCall[0] = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void initializeRecyclerViewAdapter() {
        recyclerViewAdapterPosts = new RecyclerViewAdapterPosts(viewModelPost.getSavedPosts().getValue(), requireActivity());
        RecyclerView recyclerView = binding.recyclerViewPopularBooks;
        recyclerView.setAdapter(recyclerViewAdapterPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

}
