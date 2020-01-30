package com.example.bookapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class AdapterRecipeSuggestion extends RecyclerView.Adapter<AdapterRecipeSuggestion.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private Context context;
    private ActionsInterface actionsInterface;

    public AdapterRecipeSuggestion(@NonNull ArrayList<Recipe> recipes, Activity activity) {
        this.recipes = recipes;
        actionsInterface = (ActionsInterface) activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_recipe_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(recipes.get(position).getImageUrl())
                .into(holder.recipeImage);
        holder.recipeName.setText(recipes.get(position).getName());
        //when the user clicks on an item
        //display the extended item fragment
        holder.itemView.setOnClickListener(view -> actionsInterface.expandRecipe(recipes.get(position)));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //declare the views to bind
        private ImageView recipeImage;
        private TextView recipeName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            bindValuesToViews(itemView);
        }

        private void bindValuesToViews(View itemLayout) {
            recipeImage = itemLayout.findViewById(R.id.image_recipe_grid_item);
            recipeName = itemLayout.findViewById(R.id.name_recipe_grid_item);
        }
    }
}
