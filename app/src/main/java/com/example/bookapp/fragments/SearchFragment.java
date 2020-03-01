package com.example.bookapp.fragments;

import android.os.Bundle;
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
import com.example.bookapp.viewModels.ViewModelPost;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";
    private SearchView searchView;
    private SearchFragmentInterface searchFragmentInterface;
    private LiveData<ArrayList<Post>> autocompleteSuggestions;
    private LiveData<ArrayList<Post>> previousAutocompleteSuggestion;
    private FragmentSearchBinding binding;
    private SuggestionsAdapter suggestionsRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onViewCreated(container,savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        searchView = binding.searchView;
        configureRecyclerView();
        configureSearch();
        attachObserverToViewModel();
        return binding.getRoot();
    }

    private void configureRecyclerView() {
        //this should be replaced by search history
        suggestionsRecyclerView = new SuggestionsAdapter(new ArrayList<>(), getActivity());
        binding.recyclerViewSearchResults.setAdapter(suggestionsRecyclerView);
        binding.recyclerViewSearchResults.setHasFixedSize(true);
        binding.recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSearchResults.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void attachObserverToViewModel() {
        ViewModelPost viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        autocompleteSuggestions = viewModelPost.getAutocompleteResults();
        previousAutocompleteSuggestion = viewModelPost.getPreviousAutocompleteResults();
        viewModelPost.getAutocompleteResults().observe(getViewLifecycleOwner(),  item ->{
                // Update the UI.
            if (autocompleteSuggestions.getValue().isEmpty()) {
                suggestionsRecyclerView.setData(previousAutocompleteSuggestion.getValue());
            } else {
                suggestionsRecyclerView.setData(autocompleteSuggestions.getValue());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFragmentInterface = (SearchFragmentInterface) getActivity();
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
                if (!query.trim().equals("")) {
                    searchFragmentInterface.performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                if (!newQuery.trim().equals("")) {
                    previousAutocompleteSuggestion.getValue().clear();
                    previousAutocompleteSuggestion.getValue().addAll(previousAutocompleteSuggestion.getValue());
                    searchFragmentInterface.fetchSuggestions(newQuery);
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
