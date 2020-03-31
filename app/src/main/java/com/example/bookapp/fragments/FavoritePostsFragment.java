package com.example.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.RecyclerViewAdapterPosts;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentPostsDataBinding;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;
import com.example.bookapp.viewModels.ViewModelPost;
import com.example.bookapp.viewModels.ViewModelUser;

import java.util.ArrayList;

public class FavoritePostsFragment extends Fragment {

    private RecyclerViewAdapterPosts recyclerViewAdapterPosts;
    private LayoutFragmentPostsDataBinding binding;
    private ViewModelPost viewModelPost;
    private ViewModelUser viewModelUser;
    private ArrayList<Post> favoritePosts;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initializeViews(inflater, container);
            attachObserver();

        bindDataToViews();
        return binding.getRoot();
    }

    private void initializeViews(@NonNull LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_posts_data, container, false);
        initializeRecyclerView();
        initializeRecyclerViewAdapter();
    }

    private void attachObserver() {
        viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);

        User user = viewModelUser.getUser().getValue();
        if (user != null) {
            viewModelPost.getFavoritePosts().observe(getViewLifecycleOwner(), favoritePosts -> {
                recyclerViewAdapterPosts.addData(new ArrayList<>(favoritePosts));
                bindDataToViews();
            });
        }
    }


    private void bindDataToViews() {
        if (favoritePosts == null) {
            return;
        }
        if (favoritePosts.isEmpty()) {
            return;

        }

        binding.numberResults.setText(getString(R.string.number_saved_posts, favoritePosts.size()));
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


    private void initializeRecyclerViewAdapter() {
        if (recyclerViewAdapterPosts == null) {
            recyclerViewAdapterPosts = new RecyclerViewAdapterPosts(getResources().getStringArray(R.array.sort_parameters));
        }
        binding.recyclerViewPopularBooks.setAdapter(recyclerViewAdapterPosts);
    }

    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void initializeRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewPopularBooks;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

}
