package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;

import java.util.ArrayList;

public class AdapterRecyclerViewIngredients extends RecyclerView.Adapter<AdapterRecyclerViewIngredients.ViewHolder> {

    private ArrayList<String> ingredients;

    public AdapterRecyclerViewIngredients(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient,parent,false);

        return new ViewHolder(layout) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.ingredientName.setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_text_view_item);
        }
    }
}
