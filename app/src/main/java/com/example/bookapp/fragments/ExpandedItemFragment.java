package com.example.bookapp.fragments;


import android.os.Bundle;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.Map;

public class ExpandedItemFragment extends Fragment {
    private static final String KEY_EXPANDED_ITEM = "KEY_EXPANDED_ITEM";
    private ImageView recipeImage;
    private TextView recipeName;
    private TextView cookingTime;
    private TextView healthPoints;
    private TextView numberPeople;
    private LinearLayout features;
    private TextView dishType;
    private ViewPager viewPager;


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
        initialiseViews(layout);
        bindDataToView(getArguments().getParcelable(KEY_EXPANDED_ITEM));
        return layout;
    }

    private void initialiseViews(View layout) {
        recipeImage = layout.findViewById(R.id.recipe_image_expanded);
        recipeName = layout.findViewById(R.id.recipe_name_expanded);
        cookingTime = layout.findViewById(R.id.cooking_time_expanded);
        healthPoints = layout.findViewById(R.id.health_points_expanded);
        numberPeople = layout.findViewById(R.id.number_people_expanded);
        features = layout.findViewById(R.id.layout_features_expanded);
        dishType = layout.findViewById(R.id.dish_type);
        viewPager = layout.findViewById(R.id.view_pager_expanded);
    }

    private void bindDataToView(Recipe recipe) {
        Glide.with(getContext())
                  .load("https://spoonacular.com/recipeImages/318041-556x370.jpeg")
                  .centerInside()
                .into(recipeImage);
        recipeName.setText(recipe.getName());
        //todo
        //this operaton should not concern the view
        cookingTime.setText(Integer.toString(recipe.getCookingTime()));
        healthPoints.setText(Integer.toString(recipe.getHealthPoints()));
        numberPeople.setText(Integer.toString(recipe.getNumberOfPeople()));
        dishType.setText(recipe.getDishType());
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    return FragmentRecyclerView.getInstance(recipe.getIngredients());
                }else{
                    if(position==1){
                        return FragmentRecyclerView.getInstance(recipe.getIngredients());
                    }
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "Ingredients";
                }else{
                    return "Instructions";
                }
            }
        });

        for (Map.Entry<String,Boolean> mapElement : recipe.getFeatures().entrySet()) {

           if(mapElement.getValue()){
              TextView featureTextView  = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text_view,null);
              features.addView(featureTextView);
           }
        }

    }

}
