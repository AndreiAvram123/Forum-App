package com.example.bookapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutGridRecipeItemBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Post> data;
    private MainActivityInterface mainActivityInterface;

    public GridAdapter(Activity activity) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data = data;
        mainActivityInterface = (MainActivityInterface) activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
        notifyDataSetChanged();
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
        @SuppressLint("ViewHolder") LayoutGridRecipeItemBinding  binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_grid_recipe_item, parent, false);

        binding.setPost(data.get(position));
        Glide.with(parent.getContext())
                .load(data.get(position).getPostImage())
                .into(binding.imageRecipeGridItem);
        binding.getRoot().setOnClickListener(view -> mainActivityInterface.expandPost(data.get(position).getPostID()));
        return binding.getRoot();
    }
}
