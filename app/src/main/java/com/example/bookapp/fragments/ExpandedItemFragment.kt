package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
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
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class ExpandedItemFragment : Fragment(), CommentDialogInterface {
    private lateinit var post: Post;
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
        user = viewModelUser.user.value

        return binding.root
    }

    private fun attachObservers() {
        user?.let {
            viewModelUser.user.observe(viewLifecycleOwner, Observer { user = it })
        }

        val postID = ExpandedItemFragmentArgs.fromBundle(requireArguments()).postID

        viewModelPost.getPostByID(postID).observe(viewLifecycleOwner, Observer { fetchedPost: Post ->
                post = fetchedPost
                configureViews()
        })
        viewModelComments.getCommentsForPost(postID).observe(viewLifecycleOwner, Observer { fetchedComments: ArrayList<Comment>? ->
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
        if (user != null)
            favoriteButton!!.setOnClickListener {
                post.isFavorite = !post.isFavorite
                binding.notifyChange()
                if (post.isFavorite) {
                    informUserPostRemovedFromFavorites()
                    viewModelPost.deletePostFromFavorites(post, user?.userID)
                } else {
                    informUserPostAddedToFavorites()
                    viewModelPost.addPostToFavorites(post, user?.userID)
                }
            }
        binding.backButtonExpanded.setOnClickListener { Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack() }
        binding.writeCommentButton.setOnClickListener { showCommentDialog() }
    }

    private fun showCommentDialog() {
        commentDialog = CommentDialog(requireActivity(), this, post.postID)
        commentDialog!!.show()
    }

    private fun informUserPostAddedToFavorites() {
        Snackbar.make(binding.root, getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT).show()
    }

    private fun informUserPostRemovedFromFavorites() {
        Snackbar.make(binding.root, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show()
    }

    override fun submitComment(comment: String, postID: Long) {
        user?.let {
            val serializeComment = SerializeComment(postID, comment, it.userID)
            viewModelComments.uploadComment(serializeComment)
        }
    }

    override fun onStop() {
        super.onStop()
        commentDialog?.hide()

    }
}