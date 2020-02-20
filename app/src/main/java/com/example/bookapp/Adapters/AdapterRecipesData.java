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
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class AdapterRecipesData extends RecyclerView.Adapter<AdapterRecipesData.ViewHolder> {
    private ArrayList<Post> posts;
    private Context context;
    private ActionsInterface actionsInterface;
    private String[] allSortCriteria;

    public AdapterRecipesData(@NonNull ArrayList<Post> posts, Activity activity) {
        this.posts = posts;
        actionsInterface = (ActionsInterface) activity;
        allSortCriteria = activity.getResources().getStringArray(R.array.sort_parameters);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipe, parent, false);

        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeName.setText(posts.get(position).getPostTitle());
        Glide.with(context)
                .load(posts.get(position).getPostImage())
                .centerInside()
                .into(holder.recipeImage);
        //when the user clicks on an item
        //display the extended item fragment
        holder.itemView.setOnClickListener(view -> actionsInterface.expandPost(posts.get(position).getPostID()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void sort(String sortCriteria) {

//        if (sortCriteria.equals(allSortCriteria[1])) {
//            //quickest
//            posts.sort((r1, r2) -> {
//                int r1CookingTime = Integer.parseInt(r1.getReadyInMinutes());
//                int r2CookingTime = Integer.parseInt(r2.getReadyInMinutes());
//                return Integer.compare(r1CookingTime, r2CookingTime);
//            });
//            notifyDataSetChanged();
//        }
//        if (sortCriteria.equals(allSortCriteria[2])) {
//            //Number of servings
//            posts.sort((r1, r2) -> {
//                int r1Servings = Integer.parseInt(r1.getServings());
//                int r2Servings = Integer.parseInt(r2.getServings());
//                //the compare method return 1 if the first number is smaller than
//                //the first one
//                return Integer.compare(r2Servings, r1Servings);
//            });
//            notifyDataSetChanged();
//        }

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
            recipeImage = itemLayout.findViewById(R.id.recipe_image_list_item);
            recipeName = itemLayout.findViewById(R.id.recipe_name_list_item);
        }
    }
}
