package com.example.bookapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.databinding.CommentItemListBinding;
import com.example.bookapp.models.Comment;

import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolder> {
    private ArrayList<Comment> comments;

    public AdapterComments(@NonNull ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CommentItemListBinding commentItemListBinding = CommentItemListBinding.inflate(inflater, parent, false);
        return new ViewHolder(commentItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindDataToView(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CommentItemListBinding commentItemListBinding;

        ViewHolder(@NonNull CommentItemListBinding commentItemListBinding) {
            super(commentItemListBinding.getRoot());
            this.commentItemListBinding = commentItemListBinding;
        }

        void bindDataToView(Comment comment) {
            commentItemListBinding.setComment(comment);
        }
    }
}
