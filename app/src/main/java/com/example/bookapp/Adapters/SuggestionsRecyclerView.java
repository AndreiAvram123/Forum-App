package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;

import java.util.ArrayList;

public class SuggestionsRecyclerView  extends RecyclerView.Adapter<SuggestionsRecyclerView.ViewHolder> {
   private ArrayList<String>suggestions;

   public SuggestionsRecyclerView(ArrayList<String> suggestions){
       this.suggestions = suggestions;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.suggestionName.setText(suggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      ImageView deleteSuggestionImage;
      TextView suggestionName;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           deleteSuggestionImage = itemView.findViewById(R.id.delete_suggestion_image);
           suggestionName = itemView.findViewById(R.id.suggestion_item_name);
           deleteSuggestionImage.setOnClickListener(view->{});
       }
   }
}
