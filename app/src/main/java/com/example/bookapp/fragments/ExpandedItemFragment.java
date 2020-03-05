package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;
import com.example.bookapp.activities.AppUtilities;
import com.example.bookapp.customViews.CommentDialog;
import com.example.bookapp.databinding.FragmentExpandedItemBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.Comment;
import com.example.bookapp.models.CommentBuilder;
import com.example.bookapp.models.Post;
import com.example.bookapp.viewModels.ViewModelPost;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ExpandedItemFragment extends Fragment implements CommentDialog.CommentDialogInterface {
    private MainActivityInterface mainActivityInterface;
    private Post post;
    private ArrayList<Comment> comments;
    private ImageView saveButton;
    private FragmentExpandedItemBinding binding;
    private FragmentActivity activity;
    private CommentDialog commentDialog;
    private CommentsFragment commentsFragment;
    private ViewModelPost viewModelPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expanded_item, container, false);

        if (viewModelPost == null) {
            viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
            post = viewModelPost.getCurrentPost().getValue();
            comments = viewModelPost.getCurrentPostComments().getValue();
        }

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
                informUserPostRemovedFromFavorites();
                mainActivityInterface.deleteSavedPost(post);
            } else {
                informUserPostAddedToFavorites();
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

    private void informUserPostAddedToFavorites() {
        post.setSaved(true);
        saveButton.setImageResource(R.drawable.ic_favorite_red_32dp);
        Snackbar.make(binding.getRoot(), "Recipe added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    private void informUserPostRemovedFromFavorites() {
        post.setSaved(false);
        saveButton.setImageResource(R.drawable.ic_favorite_border_black_32dp);
        Snackbar.make(binding.getRoot(), "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void submitComment(String comment, int postID) {
        CommentBuilder commentBuilder = new CommentBuilder();
        String username = "Andrei Avram";
        commentBuilder.setCommentID(1000)
                .setCommentDate(AppUtilities.getDateString())
                .setCommentContent(comment)
                .setPostID(postID)
                .setCommentAuthor(username);
        commentsFragment.addComment(commentBuilder.createComment());
        mainActivityInterface.uploadComment(commentBuilder.createComment());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (commentDialog != null) {
            commentDialog.hide();
        }
    }
}
