package com.example.bookapp.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.interfaces.ActionsInterface;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Recipe> data;
    private ActionsInterface actionsInterface;

    public GridAdapter(@NonNull ArrayList<Recipe> data, Activity activity) {
        this.data = data;
        actionsInterface = (ActionsInterface) activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_recipe_item, parent, false);
        }
        TextView recipeName = convertView.findViewById(R.id.name_recipe_grid_item);
        recipeName.setText(data.get(position).getName());
        ImageView recipeImage = convertView.findViewById(R.id.image_recipe_grid_item);
        Glide.with(parent.getContext())
                .load(data.get(position).getImageUrl())
                .into(recipeImage);
         convertView.setOnClickListener(view->actionsInterface.expandRecipe(data.get(position)));
        return convertView;
    }
}
