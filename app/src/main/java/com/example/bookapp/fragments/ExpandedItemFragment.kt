package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.example.bookapp.customViews.CommentDialog
import com.example.bookapp.customViews.CommentDialog.CommentDialogInterface
import com.example.bookapp.databinding.FragmentExpandedItemBinding
import com.example.bookapp.models.Comment
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelComments
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.dataObjectsToSerialize.SerializeComment
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ExpandedItemFragment : Fragment(), CommentDialogInterface {
    private var post: Post? = null
    private var comments: ArrayList<Comment>? = null
    private var favoriteButton: ImageView? = null
    lateinit var binding: FragmentExpandedItemBinding
    private var commentDialog: CommentDialog? = null
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelComments: ViewModelComments by activityViewModels()
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_expanded_item, container, false)
        attachObservers()
        return binding.root
    }

    private fun attachObservers() {
        user = viewModelUser!!.user.value
        //start observing if the user is not logged in to see
//weather he will log in
        if (user == null) {
            viewModelUser.user.observe(viewLifecycleOwner, Observer { newUser: User? -> user = newUser })
        }
        val postID = ExpandedItemFragmentArgs.fromBundle(requireArguments()).postID
        viewModelPost.getPost(postID).observe(viewLifecycleOwner, Observer { fetchedPost: Post? ->
                post = fetchedPost
                configureViews()
        })
        viewModelComments!!.getCommentsForPost(postID).observe(viewLifecycleOwner, Observer { fetchedComments: ArrayList<Comment>? ->
            comments = fetchedComments
            displayCommentsFragment()
        })
    }

    private fun displayCommentsFragment() {
        val commentsFragment = CommentsFragment.getInstance(comments)
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_comments_fragment, commentsFragment)
                .commit()
    }

    private fun configureViews() {
        binding.post = post
        favoriteButton = binding.saveButtonExpanded
        if (user != null) {
            favoriteButton!!.setOnClickListener { view: View? ->
                if (post!!.isFavorite) {
                    post!!.isFavorite = false
                    informUserPostRemovedFromFavorites()
                    viewModelPost.deletePostFromFavorites(post!!, user!!.userID)
                } else {
                    post!!.isFavorite = true
                    informUserPostAddedToFavorites()
                    viewModelPost.addPostToFavorites(post!!, user!!.userID)
                }
            }
        } else { //todo
//prompt user to log in
        }
        binding!!.backButtonExpanded.setOnClickListener { view: View? -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack() }
        binding!!.writeCommentButton.setOnClickListener { view: View? -> showCommentDialog() }
        Glide.with(requireContext())
                .load(post?.postImage)
                .centerInside()
                .into(binding!!.recipeImageExpanded)
    }

    private fun showCommentDialog() {
        commentDialog = CommentDialog(requireActivity(), this, post!!.postID)
        commentDialog!!.show()
    }

    private fun informUserPostAddedToFavorites() {
        post!!.isFavorite = true
        Snackbar.make(binding!!.root, "Recipe added to favorites", Snackbar.LENGTH_SHORT).show()
    }

    private fun informUserPostRemovedFromFavorites() {
        post!!.isFavorite = false
        Snackbar.make(binding!!.root, "Recipe deleted from favorites", Snackbar.LENGTH_SHORT).show()
    }

    override fun submitComment(comment: String, postID: Long) {
        if (user != null) {
            val serializeComment = SerializeComment(postID, comment, user!!.userID)
            viewModelComments.uploadComment(serializeComment)
        }
    }

    override fun onStop() {
        super.onStop()
        if (commentDialog != null) {
            commentDialog!!.hide()
        }
    }
}