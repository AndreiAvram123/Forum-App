package com.example.bookapp.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
   private ArrayList<Recipe> recipes;
   private Context context;

    public AdapterRecyclerView(@NonNull ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe,parent,false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.recipeName.setText(recipes.get(position).getName());
       Glide.with(context)
               .load(recipes.get(position).getImageUrl())
               .centerInside()
               .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
    //declare the views to bind
     private ImageView recipeImage;
     private TextView recipeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindValuesToViews(itemView);
        }
        private void bindValuesToViews(View itemLayout){
          recipeImage = itemLayout.findViewById(R.id.recipe_image_list_item);
          recipeName = itemLayout.findViewById(R.id.recipe_name_list_item);
        }
    }
}
