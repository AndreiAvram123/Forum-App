package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookapp.Adapters.AdapterRecipeSuggestion;
import com.example.bookapp.R;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.Recipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ExpandedItemFragment extends Fragment {
    private static final String KEY_EXPANDED_ITEM = "KEY_EXPANDED_ITEM";
    private static final String KEY_SIMILAR_ITEMS = "KEY_SIMILAR_ITEMS";
    private ImageView postImage;
    private TextView postTitle;
    private TextView postDate;
    private TextView postAuthor;
    private TextView numberOfLikes;
    private LinearLayout features;
    private TextView dishType;
    private ViewPager viewPager;
    private RecyclerView listRecipeSuggestions;
    private ActionsInterface actionsInterface;
    private Post post;
    private ArrayList<Recipe> recipeSuggestions;
    private StringDataFragment fragmentIngredients;
    private StringDataFragment fragmentInstructions;
    private ImageView saveButton;
    private View layout;


    public static ExpandedItemFragment getInstance(@NonNull Post selectedPost,
                                                   @Nullable ArrayList<Recipe> similarRecipes) {

        ExpandedItemFragment expandedItemFragment = new ExpandedItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EXPANDED_ITEM, selectedPost);
        bundle.putParcelableArrayList(KEY_SIMILAR_ITEMS, similarRecipes);
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
        layout = inflater.inflate(R.layout.fragment_expanded_item, container, false);
        post = getArguments().getParcelable(KEY_EXPANDED_ITEM);
        //recipeSuggestions = getArguments().getParcelableArrayList(KEY_SIMILAR_ITEMS);
        if (post != null) {
            initialiseViews();
            bindDataToView(post);
            actionsInterface = (ActionsInterface) getActivity();
        }
        //todo

        if (recipeSuggestions != null) {
            listRecipeSuggestions.setAdapter(new AdapterRecipeSuggestion(recipeSuggestions, getActivity()));
            listRecipeSuggestions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            listRecipeSuggestions.setHasFixedSize(true);
        }
        return layout;
    }

    private void initialiseViews() {
        postImage = layout.findViewById(R.id.recipe_image_expanded);
        postTitle = layout.findViewById(R.id.recipe_name_expanded);
        postDate = layout.findViewById(R.id.date_post_expanded);
        postAuthor = layout.findViewById(R.id.author_post_expanded);
        numberOfLikes = layout.findViewById(R.id.number_likes_post_expanded);

        features = layout.findViewById(R.id.layout_features_expanded);
        dishType = layout.findViewById(R.id.dish_type);
        viewPager = layout.findViewById(R.id.view_pager_expanded);
        listRecipeSuggestions = layout.findViewById(R.id.list_item_suggested_recipes);
        saveButton = layout.findViewById(R.id.save_button_expanded);
        ImageView backButton = layout.findViewById(R.id.back_button_expanded);
        backButton.setOnClickListener((view) -> getActivity().getSupportFragmentManager().popBackStack());
        ImageView shareButton = layout.findViewById(R.id.share_button_expanded);
        shareButton.setOnClickListener(view -> actionsInterface.sharePost(post));
        configureSaveButton();

    }

    private void configureSaveButton() {
        if (post.isSaved()) {
            saveButton.setImageResource(R.drawable.ic_favorite_red_32dp);
        }
        saveButton.setOnClickListener(view -> {
            if (post.isSaved()) {
                actionsInterface.deleteSavedPost(post);
            } else {
                actionsInterface.savePost(post);
            }

        });
    }

    public void informUserPostAddedToFavorited() {
        post.setSaved(true);
        saveButton.setImageResource(R.drawable.ic_favorite_red_32dp);
        Snackbar.make(layout, "Recipe added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    public void informUserPostRemovedFromFavorites() {
        post.setSaved(false);
        saveButton.setImageResource(R.drawable.ic_favorite_border_black_32dp);
        Snackbar.make(layout, "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show();
    }

    private void bindDataToView(Post post) {
        Glide.with(getContext())
                .load(post.getPostImage())
                .centerInside()
                .into(postImage);
        postTitle.setText(post.getPostTitle());
        postDate.setText(post.getPostDate());
        postAuthor.setText(post.getPostAuthor());


        //the BEHAVIOR_RESUME_ONLY flag need to be set
//        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//            @Override
//            public Fragment getItem(int position) {
//                if (position == 0) {
//                    return fragmentIngredients;
//                } else {
//                    if (position == 1) {
//                        return fragmentInstructions;
//                    }
//                }
//                return fragmentIngredients;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//
//            @Nullable
//            @Override
//            public CharSequence getPageTitle(int position) {
//                if (position == 0) {
//                    return "Ingredients";
//                } else {
//                    return "Instructions";
//                }
//            }
//        });
//
//        for (Map.Entry<String, Boolean> mapElement : recipe.getFeatures().entrySet()) {
//
//            if (mapElement.getValue()) {
//                TextView featureTextView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text_view, null);
//                features.addView(featureTextView);
//            }
//        }

    }


}
