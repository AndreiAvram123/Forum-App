package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String KEY_RECIPES = "KEY_RECIPES";

    public static MainFragment getInstance(ArrayList<Recipe> recipes){
      MainFragment mainFragment = new MainFragment();
      Bundle bundle = new Bundle();
      bundle.putParcelableArrayList(KEY_RECIPES,recipes);
      mainFragment.setArguments(bundle);
      return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main_1, container, false);
    }

}
