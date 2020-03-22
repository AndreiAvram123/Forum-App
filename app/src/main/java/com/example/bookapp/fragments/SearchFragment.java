package com.example.bookapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookapp.Adapters.SuggestionsAdapter;
import com.example.bookapp.R;
import com.example.bookapp.databinding.FragmentSearchBinding;
import com.example.bookapp.models.Post;
import com.example.bookapp.viewModels.ViewModelSearch;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private LiveData<ArrayList<Post>> autocompleteSuggestions;
    private FragmentSearchBinding binding;
    private SuggestionsAdapter suggestionsAdapter;
    private ViewModelSearch viewModelSearch;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onViewCreated(container,savedInstanceState);
        if (viewModelSearch == null) {
            attachObserverToViewModel();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        searchView = binding.searchView;
        configureRecyclerView();
        configureSearch();
        attachObserverToViewModel();
        return binding.getRoot();
    }

    private void configureRecyclerView() {
        //this should be replaced by search history
        suggestionsAdapter = new SuggestionsAdapter(new ArrayList<>());
        binding.recyclerViewSearchResults.setAdapter(suggestionsAdapter);
        binding.recyclerViewSearchResults.setHasFixedSize(true);
        binding.recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSearchResults.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void attachObserverToViewModel() {
        viewModelSearch = new ViewModelProvider(requireActivity()).get(ViewModelSearch.class);
        viewModelSearch.getObservableSearchSuggestions().observe(getViewLifecycleOwner(), suggestions -> {

            suggestionsAdapter.setData(suggestions);
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void clearSearch() {
        searchView.onActionViewCollapsed();
        searchView.setBackground(getActivity().getDrawable(R.drawable.search_background));
    }

    private void configureSearch() {
        searchView.setOnClickListener(view -> {
            if (searchView.isIconified()) {
                searchView.setBackground(getActivity().getDrawable(R.drawable.search_background_highlighted));
                searchView.setIconified(false);
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                if (!newQuery.trim().equals("")) {
                    if (!newQuery.trim().equals("") && newQuery.length() > 1) {
                        viewModelSearch.requestSuggestions(newQuery);
                    }
                } else {
                    suggestionsAdapter.setData(new ArrayList<>());
                }

                return false;
            }

        });
    }


    public interface SearchFragmentInterface {
        void performSearch(String query);

        void fetchSuggestions(String query);

        void fetchSelectedPostById(int id);
    }
}
