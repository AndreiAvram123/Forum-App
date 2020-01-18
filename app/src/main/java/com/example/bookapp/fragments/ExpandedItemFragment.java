package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.FileUriExposedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

public class ExpandedItemFragment extends Fragment {
    private static final String KEY_EXPANDED_ITEM = "KEY_EXPANDED_ITEM";

    public static ExpandedItemFragment getInstance(Recipe selectedRecipe){

        ExpandedItemFragment expandedItemFragment = new ExpandedItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EXPANDED_ITEM,selectedRecipe);
        expandedItemFragment.setArguments(bundle);
        return expandedItemFragment;
    }

    public ExpandedItemFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_expanded_item, container, false);
        bindDataToView(layout,getArguments().getParcelable(KEY_EXPANDED_ITEM));
        return layout;
    }

    private void bindDataToView(View layout,Recipe recipe) {
        ImageView recipeImage = layout.findViewById(R.id.recipe_image_extended);
        Glide.with(getContext())
                  .load("https://spoonacular.com/recipeImages/318041-556x370.jpeg")
                  .centerInside()
                .into(recipeImage);

    }

}
