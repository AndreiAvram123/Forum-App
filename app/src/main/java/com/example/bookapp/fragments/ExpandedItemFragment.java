package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookapp.Adapters.AdapterRecipeSuggestion;
import com.example.bookapp.MainActivity;
import com.example.bookapp.R;
import com.example.bookapp.databinding.FragmentExpandedItemBinding;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.Recipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ExpandedItemFragment extends Fragment {
    private static final String KEY_EXPANDED_ITEM = "KEY_EXPANDED_ITEM";
    private static final String KEY_SIMILAR_ITEMS = "KEY_SIMILAR_ITEMS";
    private ActionsInterface actionsInterface;
    private Post post;
    private ImageView saveButton;
    private FragmentExpandedItemBinding binding;

    public static ExpandedItemFragment getInstance(@NonNull Post selectedPost,
                                                   @Nullable ArrayList<Recipe> similarRecipes) {

        ExpandedItemFragment expandedItemFragment = new ExpandedItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EXPANDED_ITEM, selectedPost);
        bundle.putParcelableArrayList(KEY_SIMILAR_ITEMS, similarRecipes);
        expandedItemFragment.setArguments(bundle);

        return expandedItemFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expanded_item, container, false);
        post = getArguments().getParcelable(KEY_EXPANDED_ITEM);
        //recipeSuggestions = getArguments().getParcelableArrayList(KEY_SIMILAR_ITEMS);
        if (post != null) {
            binding.setPost(post);
            saveButton = binding.saveButtonExpanded;
            configureViews();
            actionsInterface = (ActionsInterface) getActivity();


        }

        //  if (recipeSuggestions != null) {
        // listRecipeSuggestions.setAdapter(new AdapterRecipeSuggestion(recipeSuggestions, getActivity()));
        // listRecipeSuggestions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //listRecipeSuggestions.setHasFixedSize(true);
        // }
        return binding.getRoot();
    }

    private void configureViews() {

        saveButton.setOnClickListener(view -> {
            if (post.isSaved()) {
                actionsInterface.deleteSavedPost(post);
            } else {
                actionsInterface.savePost(post);
            }

        });
        binding.backButtonExpanded.setOnClickListener((view) -> getActivity().getSupportFragmentManager().popBackStack());

        Glide.with(getContext())
                .load(post.getPostImage())
                .centerInside()
                .into(binding.recipeImageExpanded);
    }

    public void informUserPostAddedToFavorited() {
        post.setSaved(true);
        saveButton.setImageResource(R.drawable.ic_favorite_red_32dp);
        Snackbar.make(binding.getRoot(), "Recipe added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    public void informUserPostRemovedFromFavorites() {
        post.setSaved(false);
        saveButton.setImageResource(R.drawable.ic_favorite_border_black_32dp);
        Snackbar.make(binding.getRoot(), "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show();
    }

    private void bindDataToView() {

//        for (Map.Entry<String, Boolean> mapElement : recipe.getFeatures().entrySet()) {
//
//            if (mapElement.getValue()) {
//                TextView featureTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text_view, null);
//                features.addView(featureTextView);
//            }
//        }

    }


}
