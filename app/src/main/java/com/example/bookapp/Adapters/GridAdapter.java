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
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Post> data;
    private MainActivityInterface mainActivityInterface;

    public GridAdapter(@NonNull ArrayList<Post> data, Activity activity) {
        this.data = data;
        mainActivityInterface = (MainActivityInterface) activity;
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
        TextView postName = convertView.findViewById(R.id.name_recipe_grid_item);
        postName.setText(data.get(position).getPostTitle());
        ImageView recipeImage = convertView.findViewById(R.id.image_recipe_grid_item);
        Glide.with(parent.getContext())
                .load(data.get(position).getPostImage())
                .into(recipeImage);
        convertView.setOnClickListener(view -> mainActivityInterface.expandPost(data.get(position).getPostID()));
        return convertView;
    }
}
