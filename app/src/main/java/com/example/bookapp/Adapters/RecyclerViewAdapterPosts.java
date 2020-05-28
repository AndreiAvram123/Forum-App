package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutItemPostBinding;
import com.example.bookapp.fragments.ExpandedItemFragmentDirections;
import com.example.bookapp.models.Post;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterPosts extends RecyclerView.Adapter<RecyclerViewAdapterPosts.ViewHolder> {
    private ArrayList<Post> posts = new ArrayList<>();
    private String[] allSortCriteria;

    public RecyclerViewAdapterPosts(@NonNull String[] allSortCriteria) {
        this.allSortCriteria = allSortCriteria;
    }

    public void addData(List<Post> data) {
        this.posts = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_item_post, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setPost(posts.get(position));
        holder.itemView.setOnClickListener(view -> {
            NavDirections action = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(posts.get(position).getId());
            Navigation.findNavController(holder.binding.getRoot()).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void sort(String sortCriteria) {


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemPostBinding binding;

        ViewHolder(@NonNull LayoutItemPostBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }

    }
}
