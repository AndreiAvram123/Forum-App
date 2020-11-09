package com.andrei.kit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.AdapterComments
import com.andrei.kit.R
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.bottomSheets.CommentBottomSheet
import com.andrei.kit.databinding.PostExpandedFragmentBinding
import com.andrei.kit.models.Comment
import com.andrei.kit.models.Post
import com.andrei.kit.models.User
import com.andrei.kit.viewModels.ViewModelComments
import com.andrei.kit.viewModels.ViewModelPost
import com.andrei.dataLayer.models.serialization.SerializeComment
import com.andrei.kit.utils.reObserve
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


@InternalCoroutinesApi
@AndroidEntryPoint
class ExpandedPostFragment : Fragment() {
    lateinit var binding: PostExpandedFragmentBinding

    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelComments: ViewModelComments by activityViewModels()

    @Inject
    lateinit var user: User

    lateinit var post: Post
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



        viewModelPost.getPostByID(args.postID).reObserve(viewLifecycleOwner, {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.reObserve(viewLifecycleOwner,{newPost ->
                        post = newPost
                        configureViews()
                        getComments()
                    })
                }
                Status.LOADING ->{

                }
                Status.ERROR ->{

                }
            }

        })

    }

    private fun getComments() {
        viewModelComments.getCommentsForPost(post).observe(viewLifecycleOwner, {
            insertComments(ArrayList(it.comments))
        })
    }

    private fun insertComments(comments: ArrayList<Comment>) {
        val adapterComments = AdapterComments(comments)

        binding.recyclerComments.apply {
            adapter = adapterComments
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
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
        binding.postImageExpanded.setOnClickListener {
            val action = ExpandedPostFragmentDirections.actionGlobalImageZoomFragment(post.image, false)
            findNavController().navigate(action)
        }
    }

    private fun showCommentSheet() {
        commentDialog.show(requireActivity().supportFragmentManager, "commentSheet")
    }

    private fun submitComment(content: String) {
        val commentToUpload = SerializeComment(content = content,
                postID = post.id,
                userID = user.userID)

        viewModelComments.uploadComment(commentToUpload).reObserve(viewLifecycleOwner, {
            when(it.status){
                Status.LOADING->{

                }
                Status.SUCCESS->{

                }
                Status.ERROR ->{

                }

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