package com.andrei.kit.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
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
import com.andrei.kit.utils.isConnected
import com.andrei.kit.utils.observeOnce
import com.andrei.kit.utils.observeRequest
import com.andrei.kit.utils.reObserve
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jama.carouselview.enums.IndicatorAnimationType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ExpandedPostFragment : Fragment() {
    lateinit var binding: PostExpandedFragmentBinding

    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelComments: ViewModelComments by activityViewModels()


    @Inject
    lateinit var user: User

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    lateinit var post: Post
    private val commentButtonSheet  by lazy {
        CommentBottomSheet(::submitComment)
    }

    private val adapterComments by lazy{
        AdapterComments()
    }

    private val args: ExpandedPostFragmentArgs by navArgs()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment

            binding = PostExpandedFragmentBinding.inflate(layoutInflater, container, false)
            attachObservers()
            configureRecycleView()
            return binding.root
        }

    private fun configureRecycleView() {
        binding.recyclerComments.apply {
            adapter = adapterComments
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun attachObservers() {

            viewModelPost.getPostByID(args.postID).observeRequest(viewLifecycleOwner, {
                when(it.status){
                    Status.SUCCESS ->{
                        if(it.data !=null){
                            post = it.data
                            configureViews()
                            getComments()
                        }
                    }
                    Status.LOADING ->{
                    //todo
                        //loading ui
                    }
                    Status.ERROR ->{

                    }
                }

            })

        }

        private fun getComments() {
            viewModelComments.getCommentsForPost(post).reObserve(viewLifecycleOwner, {
                adapterComments.setData(it.comments.toMutableList())
            })
        }



        private fun configureViews() {
            binding.post = post
            binding.saveButtonExpanded.setOnClickListener {
                if (post.isFavorite) {
                    viewModelPost.removeFromFavorites(post).observeOnce(viewLifecycleOwner,{
                        if(it.status == Status.SUCCESS){
                            informUserPostRemovedFromFavorites()
                            binding.post = post
                            binding.notifyChange()
                        }
                    })
                } else {
                    viewModelPost.addPostToFavorites(post).observeOnce(viewLifecycleOwner, {
                       if(it.status == Status.SUCCESS) {
                          informUserPostAddedToFavorites()
                           binding.post = post
                           binding.notifyChange()
                       }
                    })
                }
            }
            binding.backButtonExpanded.setOnClickListener { Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack() }
            binding.writeCommentButton.setOnClickListener {
                if(connectivityManager.isConnected()) {
                    showCommentSheet()
                }
            }
            binding.carouselPostExpanded.apply{
                val images = post.images.split(", ")
                size = images.size
                indicatorAnimationType = IndicatorAnimationType.SLIDE
                setCarouselViewListener { view, position ->
                    val imageView = view.findViewById<ImageView>(R.id.image_item_carousel)
                    Glide.with(imageView).load(images[position])
                            .into(imageView)
                   view.setOnClickListener {
                       val action = ExpandedPostFragmentDirections.actionGlobalImageZoomFragment(images[position], false)
                   findNavController().navigate(action)
                   }
                 }

                show()
            }

        }

        private fun showCommentSheet() {
            commentButtonSheet.show(requireActivity().supportFragmentManager, "commentSheet")
        }

        private fun submitComment(content: String) {
            val commentToUpload = SerializeComment(content = content,
                    postID = post.id,
                    userID = user.userID)

            viewModelComments.uploadComment(commentToUpload).observeRequest(viewLifecycleOwner, {
                when(it.status){
                    Status.LOADING->{
                     commentButtonSheet.showLoading()
                    }
                    else ->
                      commentButtonSheet.dismiss()


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
