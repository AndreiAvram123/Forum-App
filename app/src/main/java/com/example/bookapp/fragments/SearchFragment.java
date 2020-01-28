package com.example.bookapp.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";
    private SearchView searchView;
    private ArrayList<String> searchHistory;
    private SearchFragmentInterface searchFragmentInterface;
    private DataFragment searchResultsFragment;
    private ArrayList<Recipe>lastResults ;

    public static SearchFragment getInstance(ArrayList<String> searchHistory){
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SEARCH_HISTORY,searchHistory);
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
        searchHistory = getArguments().getStringArrayList(KEY_SEARCH_HISTORY);
        searchFragmentInterface = (SearchFragmentInterface) getActivity();
    }



    @Override
    public void onResume() {
        super.onResume();
        if(lastResults!=null){
            displaySearchResultsFragment();
        }else{
            displaySearchHistory();
        }

    }

    public void displaySearchResults(ArrayList<Recipe> results){
        //clear search
        clearSearch();
        searchResultsFragment = DataFragment.getInstance(results);
        lastResults = results;
        displaySearchResultsFragment();
    }
    private void displaySearchResultsFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_search_fragment,DataFragment.getInstance(lastResults))
                .commit();
    }
    private void displaySearchHistory(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_search_fragment,SearchSuggestionsFragment.getInstance(searchHistory))
                .commit();
    }

    private void clearSearch(){
        searchView.onActionViewCollapsed();
        searchView.setBackground(getActivity().getDrawable(R.drawable.search_background));
    }

    private void configureSearch() {
        searchView.setOnClickListener(view -> {
            if (searchView.isIconified()) {
                displaySearchHistory();
                searchView.setBackground(getActivity().getDrawable(R.drawable.search_background_highlighted));
                searchView.setIconified(false);
            }

        });
        searchView.setOnCloseListener(() -> {
            searchView.setBackground(getActivity().getDrawable(R.drawable.search_background));
            if(searchResultsFragment!=null){
              displaySearchResultsFragment();
            }else{
                displaySearchHistory();
            }
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().equals("")) {
                    searchHistory.add(query);
                    searchFragmentInterface.insertSearchInDatabase(query);
                    searchFragmentInterface.performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if(newText.trim().equals("")){
//                    displaySearchHistory();
//                }else{
//                    //todo
//                    //maybe have local suggestions?
//                }
                return false;
            }
        });
    }


    public interface SearchFragmentInterface{
        void insertSearchInDatabase(String search);
        void performSearch(String query);
    }
}
