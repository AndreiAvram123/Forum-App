package com.example.bookapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutItemPostBinding;
import com.example.bookapp.fragments.ExpandedItemFragmentDirections;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class RecyclerViewAdapterPosts extends RecyclerView.Adapter<RecyclerViewAdapterPosts.ViewHolder> {
    private ArrayList<Post> posts = new ArrayList<>();
    private Context context;
    private String[] allSortCriteria;

    public RecyclerViewAdapterPosts(@NonNull String[] allSortCriteria) {
        this.allSortCriteria = allSortCriteria;
    }

    public void setData(ArrayList<Post> data){
        this.posts = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutItemPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_post, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setPost(posts.get(position));
        Glide.with(context)
                .load(posts.get(position).getPostImage())
                .centerInside()
                .into(holder.binding.postImage);
        //when the user clicks on an item
        //display the extended item fragment
        holder.itemView.setOnClickListener(view -> {
            NavDirections action = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(posts.get(position).getPostID());
            Navigation.findNavController(holder.binding.getRoot()).navigate(action);
        });
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
        private LayoutItemPostBinding binding;

        ViewHolder(@NonNull LayoutItemPostBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }

    }
}
