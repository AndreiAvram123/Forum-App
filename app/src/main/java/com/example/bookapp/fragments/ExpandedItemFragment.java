package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookapp.AppUtilities;
import com.example.bookapp.R;
import com.example.bookapp.customViews.CommentDialog;
import com.example.bookapp.databinding.FragmentExpandedItemBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.CommentBuilder;
import com.example.bookapp.models.Post;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ExpandedItemFragment extends Fragment implements CommentDialog.CommentDialogInterface {
    public static final String KEY_EXPANDED_ITEM = "KEY_EXPANDED_ITEM";
    public static final String KEY_COMMENTS = "KEY_COMMENTS";
    private MainActivityInterface mainActivityInterface;
    private Post post;
    private ArrayList<Comment> comments;
    private ImageView saveButton;
    private FragmentExpandedItemBinding binding;
    private FragmentActivity activity;
    private CommentDialog commentDialog;
    private String username = "Andrei Avram";
    private CommentsFragment commentsFragment;

    public static ExpandedItemFragment getInstance(@NonNull Post selectedPost, @Nullable ArrayList<Comment> comments) {

        ExpandedItemFragment expandedItemFragment = new ExpandedItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EXPANDED_ITEM, selectedPost);
        bundle.putParcelableArrayList(KEY_COMMENTS, comments);
        expandedItemFragment.setArguments(bundle);

        return expandedItemFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expanded_item, container, false);
        post = getArguments().getParcelable(KEY_EXPANDED_ITEM);
        comments = getArguments().getParcelableArrayList(KEY_COMMENTS);

        if (post != null) {
            binding.setPost(post);
            saveButton = binding.saveButtonExpanded;
            configureViews();
            activity = getActivity();

            mainActivityInterface = (MainActivityInterface) activity;
        }

        if (comments != null) {
            displayCommentsFragment();
        }

        return binding.getRoot();
    }

    private void displayCommentsFragment() {
        commentsFragment = CommentsFragment.getInstance(comments);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_comments_fragment, commentsFragment)
                .commit();
    }

    private void configureViews() {
        if (post.isSaved()) {
            binding.saveButtonExpanded.setImageResource(R.drawable.ic_favorite_red_32dp);
        }
        saveButton.setOnClickListener(view -> {
            if (post.isSaved()) {
                mainActivityInterface.deleteSavedPost(post);
            } else {
                mainActivityInterface.savePost(post);
            }

        });
        binding.backButtonExpanded.setOnClickListener((view) -> Navigation.findNavController(activity, R.id.nav_host_fragment).popBackStack());
        binding.writeCommentButton.setOnClickListener((view) -> {
            showCommentDialog();
        });

        Glide.with(getContext())
                .load(post.getPostImage())
                .centerInside()
                .into(binding.recipeImageExpanded);
    }

    private void showCommentDialog() {
        commentDialog = new CommentDialog(activity, this, post.getPostID());
        commentDialog.show();
    }

    public void informUserPostAddedToFavorites() {
        post.setSaved(true);
        saveButton.setImageResource(R.drawable.ic_favorite_red_32dp);
        Snackbar.make(binding.getRoot(), "Recipe added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    public void informUserPostRemovedFromFavorites() {
        post.setSaved(false);
        saveButton.setImageResource(R.drawable.ic_favorite_border_black_32dp);
        Snackbar.make(binding.getRoot(), "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void submitComment(String comment, int postID) {
        CommentBuilder commentBuilder = new CommentBuilder();
        commentBuilder.setCommentID(1000)
                .setCommentDate(AppUtilities.getDateString())
                .setCommentContent(comment)
                .setPostID(postID)
                .setCommentAuthor(username);
        commentsFragment.addComment(commentBuilder.createComment());
        mainActivityInterface.uploadComment(commentBuilder.createComment());

    }
}
