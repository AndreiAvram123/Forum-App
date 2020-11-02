package com.andrei.kit.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andrei.kit.R
import com.andrei.kit.databinding.LayoutFragmentAddPostBinding
import com.andrei.kit.models.User
import com.andrei.kit.viewModels.ViewModelPost
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.dataLayer.models.serialization.SerializePost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class FragmentAddPost : Fragment() {
    private lateinit var binding: LayoutFragmentAddPostBinding
    private val CODE_FILE_EXPLORER = 10
    private val viewModelPost: ViewModelPost by activityViewModels()

    @Inject
    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_add_post, container, false)
        configureViews()

        return binding.root
    }

    private fun configureViews() {
        binding.postImageAdd.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "image/*"
            startActivityForResult(fileIntent, CODE_FILE_EXPLORER)
        }
        binding.submitPostButton.setOnClickListener {
            if (areFieldsValid()) {
                toggleUi()
                uploadPost(user)
            } else {
                displayError()
            }
        }
    }

    private fun displayError() {
        binding.errorMessageAdd.visibility = View.VISIBLE
        binding.errorMessageAdd.text = getString(R.string.fields_not_completed)
    }

    private fun areFieldsValid(): Boolean {
        val editTextContent = binding.postContentAdd
        if (editTextContent.text.isEmpty()) {
            return false
        }
        val editTextTitle = binding.postTitleAdd.text

        if (editTextTitle.isEmpty()) {
            return false
        }
        if (binding.postImageAdd.drawable == requireContext().getDrawable(R.drawable.ic_add_image)) {
            return false
        }

        return true

    }

    private fun toggleUi() {
        binding.uploadProgressBar.visibility = View.VISIBLE
        binding.postImageAdd.isEnabled = false
        binding.postContentAdd.isEnabled = false
        binding.postTitleAdd.isEnabled = false
        binding.submitPostButton.visibility = View.INVISIBLE
    }


    private fun pushPost(post: SerializePost) {
        viewModelPost.uploadPost(post).observe(viewLifecycleOwner, {
              when (it.status){
                  Status.SUCCESS ->
                 findNavController().popBackStack()
            }
        })
    }

    private fun uploadPost(user: User) {
        //get image data
        val drawable = binding.postImageAdd.drawable

        if (drawable != null) {

            lifecycleScope.launch(Dispatchers.Main) {

                viewModelPost.uploadImage(drawable).observe(viewLifecycleOwner, {
                    when (it.status){
                        Status.SUCCESS -> {
                            val post = SerializePost(
                                    title = binding.postTitleAdd.text.toString(),
                                    content = binding.postContentAdd.text.toString(),
                                    userID = user.userID,
                                    image = it.data!!
                            )
                            pushPost(post)
                        }

                    }
                })
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_FILE_EXPLORER) {
            data?.let {
                val path = it.data
                if (path != null) {
                    binding.postImageAdd.setImageURI(path)
                }
            }
        }
    }
}

