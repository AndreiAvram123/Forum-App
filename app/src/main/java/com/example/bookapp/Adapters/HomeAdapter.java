package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.databinding.PostItemHomePageBinding;
import com.example.bookapp.fragments.ExpandedItemFragmentDirections;
import com.example.bookapp.models.Post;

import java.util.ArrayList;
import java.util.Collections;

public class HomeAdapter extends RecyclerView.Adapter {
    private ArrayList<Post> posts;

    public HomeAdapter() {
        posts = new ArrayList<>();
    }

    public void addNewPosts(@NonNull ArrayList<Post> newPosts) {
        int itemsInserted = 0;
        ArrayList<Post> reversedData = new ArrayList<>(newPosts);
        Collections.reverse(reversedData);

        for (Post post : reversedData) {
            if (!posts.contains(post)) {
                posts.add(0, post);
                itemsInserted++;
            }
        }
        notifyItemRangeInserted(0, itemsInserted);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemHomePageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.post_item_home_page, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bind(posts.get(position));

        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private PostItemHomePageBinding binding;

        ViewHolder(@NonNull PostItemHomePageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Post post) {
            binding.setPost(post);
            binding.getRoot().setOnClickListener(view -> {
                NavDirections action = ExpandedItemFragmentDirections.actionGlobalExpandedItemFragment(post.getPostID());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });
            Glide.with(binding.getRoot().getContext())
                    .load(post.getPostImage())
                    .into(binding.postItemHomeImage);
        }
    }
}
