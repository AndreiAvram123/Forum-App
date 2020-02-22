package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";
    private SearchView searchView;
    private SearchFragmentInterface searchFragmentInterface;
    private ArrayList<Post> lastResults;

    public static SearchFragment getInstance(ArrayList<String> searchHistory) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SEARCH_HISTORY, searchHistory);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = layout.findViewById(R.id.searchView);
        configureSearch();
        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFragmentInterface = (SearchFragmentInterface) getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (lastResults != null) {
            displaySearchResultsFragment();
        }
    }

    public void displaySearchResults(ArrayList<Post> results) {
        //clear search
        clearSearch();
        lastResults = results;
        displaySearchResultsFragment();
    }

    private void displaySearchResultsFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_search_fragment, PostsDataFragment.getInstance(lastResults))
                .commit();
    }

    private void displaySearchSuggestions(@NonNull ArrayList<Post> suggestions) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_search_fragment, PostSuggestionsFragment.getInstance(suggestions))
                .commit();

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
        searchView.setOnCloseListener(() -> {
            searchView.setBackground(getActivity().getDrawable(R.drawable.search_background));
            if (lastResults != null) {
                displaySearchResultsFragment();
            }
            return false;
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
                    searchFragmentInterface.fetchSuggestions(newQuery);
                }

                return false;
            }

        });
    }

    public void displayFetchedSuggestions(@NonNull ArrayList<Post> suggestions) {
        //sometimes an async problem may occur when
        //the user deletes the query but the request is still not processed
        if (!searchView.getQuery().toString().trim().equals("")) {
            //check current query entered
            displaySearchSuggestions(suggestions);
        }
    }


    public interface SearchFragmentInterface {
        void performSearch(String query);
        void fetchSuggestions(String query);
        void fetchSelectedPostById(int id);
    }
}
