package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;

import java.util.ArrayList;

public class AdapterStrings extends RecyclerView.Adapter<AdapterStrings.ViewHolder> {

     ArrayList<String> data;

    public AdapterStrings(ArrayList<String> data){
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string,parent,false);
        ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.text = layout.findViewById(R.id.ingredient_text_view_item);
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
          ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
