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
import com.example.bookapp.models.User;
import com.example.bookapp.viewModels.ViewModelPost;
import com.example.bookapp.viewModels.ViewModelUser;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ExpandedItemFragment extends Fragment implements CommentDialog.CommentDialogInterface {
    private MainActivityInterface mainActivityInterface;
    private Post post;
    private ArrayList<Comment> comments;
    private ImageView favoriteButton;
    private FragmentExpandedItemBinding binding;
    private FragmentActivity activity;
    private CommentDialog commentDialog;
    private CommentsFragment commentsFragment;
    private ViewModelPost viewModelPost;
    private ViewModelUser viewModelUser;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expanded_item, container, false);

        if (viewModelPost == null) {
            attachObservers();
        }
        activity = requireActivity();
        mainActivityInterface = (MainActivityInterface) activity;

        return binding.getRoot();
    }

    private void attachObservers() {
        viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);
        user = viewModelUser.getUser().getValue();
        //start observing if the user is not logged in to see
        //weather he will log in

        if (user == null) {
            viewModelUser.getUser().observe(getViewLifecycleOwner(), newUser -> {
                this.user = newUser;
            });
        }


        int postID = ExpandedItemFragmentArgs.fromBundle(getArguments()).getPostID();
        viewModelPost.getPost(postID).observe(getViewLifecycleOwner(), fetchedPost -> {
            if (fetchedPost != null) {
                post = fetchedPost;
                configureViews();
            }
        });
        viewModelPost.getPostComments(postID).observe(getViewLifecycleOwner(), fetchedComments -> {
            if (fetchedComments != null) {
                comments = fetchedComments;
                displayCommentsFragment();
            }
        });
    }

    private void displayCommentsFragment() {
        commentsFragment = CommentsFragment.getInstance(comments);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_comments_fragment, commentsFragment)
                .commit();
    }

    private void configureViews() {
        binding.setPost(post);
        favoriteButton = binding.saveButtonExpanded;

        if (user != null) {
            favoriteButton.setOnClickListener(view -> {
                if (post.isFavorite()) {
                    post.setFavorite(false);
                    informUserPostRemovedFromFavorites();
                    viewModelPost.deletePostFromFavorites(post,user.getUserID());
                } else {
                    post.setFavorite(true);
                    informUserPostAddedToFavorites();
                    viewModelPost.addPostToFavorites(post, user.getUserID());
                }

            });
        } else {
            //todo
            //prompt user to log in
        }
        binding.backButtonExpanded.setOnClickListener((view) -> Navigation.findNavController(activity, R.id.nav_host_fragment).popBackStack());
        binding.writeCommentButton.setOnClickListener((view) -> {
            showCommentDialog();
        });

        Glide.with(requireContext())
                .load(post.getPostImage())
                .centerInside()
                .into(binding.recipeImageExpanded);
    }

    private void showCommentDialog() {
        commentDialog = new CommentDialog(activity, this, post.getPostID());
        commentDialog.show();
    }

    private void informUserPostAddedToFavorites() {
        post.setFavorite(true);
        Snackbar.make(binding.getRoot(), "Recipe added to favorites", Snackbar.LENGTH_SHORT).show();
    }

    private void informUserPostRemovedFromFavorites() {
        post.setFavorite(false);
        Snackbar.make(binding.getRoot(), "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void submitComment(String comment, int postID) {
        if (user != null) {
            CommentBuilder commentBuilder = new CommentBuilder();
            commentBuilder.setCommentID(1000)
                    .setCommentDate(AppUtilities.getDateString())
                    .setCommentContent(comment)
                    .setPostID(postID)
                    .setCommentAuthor(viewModelUser.getUser().getValue().getUsername());
            commentsFragment.addComment(commentBuilder.createComment());
            mainActivityInterface.uploadComment(commentBuilder.createComment());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (commentDialog != null) {
            commentDialog.hide();
        }
    }
}
