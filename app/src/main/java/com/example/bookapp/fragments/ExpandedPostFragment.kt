package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.AdapterComments
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class ExpandedPostFragment : Fragment(), CommentDialogInterface {
    lateinit var binding: FragmentExpandedItemBinding
    private var commentDialog: CommentDialog? = null
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelComments: ViewModelComments by activityViewModels()
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment

        binding = FragmentExpandedItemBinding.inflate(layoutInflater, container, false)
        attachObservers()
        user = viewModelUser.user.value

        return binding.root
    }

    private fun attachObservers() {
        //observer whether the user signs in
        user?.let {
            viewModelUser.user.observe(viewLifecycleOwner, Observer { user = it })
        }

        val postID: Int = ExpandedPostFragmentArgs.fromBundle(requireArguments()).postID

        viewModelPost.getPostByID(postID).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                configureViews(it)
                getComments(it);
            }
        })
    }

    private fun getComments(post: Post) {
        viewModelComments.getCommentsForPost(post).observe(viewLifecycleOwner, Observer {
            insertComments(ArrayList(it.comments))
        })
    }

    private fun insertComments(comments: ArrayList<Comment>) {
        val adapterComments = AdapterComments(comments)
        val recyclerView = binding.recyclerComments;
        recyclerView.adapter = adapterComments
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }


    private fun configureViews(post: Post) {
        binding.post = post
        if (user != null)
            binding.saveButtonExpanded.setOnClickListener {
                if (post.isFavorite) {
                    informUserPostRemovedFromFavorites()
                    user?.let { viewModelPost.deletePostFromFavorites(post, it) }
                } else {
                    informUserPostAddedToFavorites()
                    user?.let { viewModelPost.addPostToFavorites(post) }
                }
                post.isFavorite = !post.isFavorite
                binding.post = post;
                binding.notifyChange()
            }
        binding.backButtonExpanded.setOnClickListener { Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack() }
        binding.writeCommentButton.setOnClickListener { showCommentDialog() }
    }

    private fun showCommentDialog() {
        // commentDialog = CommentDialog(requireActivity(), this, post)
        //  commentDialog!!.show()
    }

    private fun informUserPostAddedToFavorites() {
        Snackbar.make(binding.root, getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT).show()
    }

    private fun informUserPostRemovedFromFavorites() {
        Snackbar.make(binding.root, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show()
    }

    override fun submitComment(comment: String, postID: Long) {
        user?.let {
            // val serializeComment = SerializeComment(postID, comment, it.userID)
            //  viewModelComments.uploadComment(serializeComment)
        }
    }

    override fun onStop() {
        super.onStop()
        commentDialog?.hide()

    }
}