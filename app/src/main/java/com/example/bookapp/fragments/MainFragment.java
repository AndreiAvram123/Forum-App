package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String KEY_RECIPES = "KEY_RECIPES";
    private static final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";
    private SearchView searchView;
    private ConstraintLayout mainContainer;
    private ArrayList<String> searchHistory;
    private MainFragmentInterface mainFragmentInterface;
    private ArrayList<Recipe> randomRecipes;

    public static MainFragment getInstance(ArrayList<Recipe> recipes,ArrayList<String> searchHistory){
      MainFragment mainFragment = new MainFragment();
      Bundle bundle = new Bundle();
      bundle.putParcelableArrayList(KEY_RECIPES,recipes);
      bundle.putStringArrayList(KEY_SEARCH_HISTORY,searchHistory);
      mainFragment.setArguments(bundle);
      return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        searchHistory = getArguments().getStringArrayList(KEY_SEARCH_HISTORY);
        randomRecipes = getArguments().getParcelableArrayList(KEY_RECIPES);
        mainFragmentInterface = (MainFragmentInterface) getActivity();
        initializeViews(layout);
        displayRandomRecipesFragment();
        return layout;
    }
    private void initializeViews(View layout) {
        mainContainer = layout.findViewById(R.id.container_main_fragment);
        searchView = layout.findViewById(R.id.searchView);
        configureSearch();

    }

    private void configureSearch() {
        searchView.setOnClickListener(view -> {
            if (searchView.isIconified()) {
                searchView.setBackground(getActivity().getDrawable(R.drawable.search_background_highlighted));
                displayOldSearchList();
                searchView.setIconified(false);
            }

        });
            searchView.setOnCloseListener(() -> {
            searchView.setBackground(getActivity().getDrawable(R.drawable.search_background));
            displayRandomRecipesFragment();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().equals("")) {
                    searchHistory.add(query);
                    mainFragmentInterface.insertSearchInDatabase(query);
                    mainFragmentInterface.performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void displayOldSearchList() {
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.container_main_fragment, SearchHistoryFragment.getInstance(searchHistory)).
                addToBackStack(null).commit();
    }

    private void displayRandomRecipesFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_fragment,FragmentRecipesList.getInstance(randomRecipes))
                .addToBackStack(null)
                .commit();
    }


    public void displaySearchResults(ArrayList<Recipe> results){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main_fragment,FragmentRecipesList.getInstance(results))
                .addToBackStack(null)
                .commit();
    }
    public interface MainFragmentInterface{
        void insertSearchInDatabase(String search);
        void performSearch(String query);
    }
}
