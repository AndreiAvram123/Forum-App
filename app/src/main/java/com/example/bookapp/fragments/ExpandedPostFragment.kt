package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.AdapterComments
import com.example.bookapp.AppUtilities
import com.example.bookapp.R
import com.example.bookapp.bottomSheets.CommentBottomSheet
import com.example.bookapp.databinding.PostExpandedFragmentBinding
import com.example.bookapp.models.Comment
import com.example.bookapp.models.Post
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelComments
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.models.serialization.SerializeComment
import com.example.dataLayer.repositories.UploadProgress
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
class ExpandedPostFragment : Fragment() {
    lateinit var binding: PostExpandedFragmentBinding

    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelComments: ViewModelComments by activityViewModels()
    private lateinit var user: User
    private lateinit var post: Post
    private val commentDialog = CommentBottomSheet(::submitComment)
    private lateinit var favoritePosts: List<Post>
    private val args: ExpandedPostFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment

        binding = PostExpandedFragmentBinding.inflate(layoutInflater, container, false)

        attachObservers()
        return binding.root
    }

    private fun attachObservers() {

        viewModelPost.getFavoritePosts().observe(viewLifecycleOwner, Observer {
            favoritePosts = it.posts
        })

        viewModelPost.getPostByID(args.postID).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if(favoritePosts.contains(it)){
                    it.isFavorite = true
                }
                post = it
                configureViews()
                getComments();
            }
        })

    }

    private fun getComments() {
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


    private fun configureViews() {
        binding.post = post
        binding.saveButtonExpanded.setOnClickListener {
            if (post.isFavorite) {
                informUserPostRemovedFromFavorites()
                viewModelPost.deletePostFromFavorites(post)

            } else {
                informUserPostAddedToFavorites()
                viewModelPost.addPostToFavorites(post)
            }
            post.isFavorite = !post.isFavorite
            binding.post = post;
            binding.notifyChange()
        }
        binding.backButtonExpanded.setOnClickListener { Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack() }
        binding.writeCommentButton.setOnClickListener { showCommentSheet() }
    }

    private fun showCommentSheet() {
        commentDialog.show(requireActivity().supportFragmentManager, "commentSheet")
    }

    private fun submitComment(content: String) {
        val commentToUpload = SerializeComment(content = content,
                postID = post.id,
                userID = user.userID)
        viewModelComments.uploadComment(commentToUpload).observe(viewLifecycleOwner, Observer {
            if (it == UploadProgress.UPLOADED) {
                commentDialog.dismiss()
            }
        })
    }

    private fun informUserPostAddedToFavorites() {
        Snackbar.make(binding.root, getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT).show()
    }

    private fun informUserPostRemovedFromFavorites() {
        Snackbar.make(binding.root, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show()
    }


}