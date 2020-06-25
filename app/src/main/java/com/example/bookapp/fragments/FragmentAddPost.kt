package com.example.bookapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutFragmentAddPostBinding
import com.example.bookapp.models.User
import com.example.bookapp.toBase64
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.serialization.SerializePost
import com.example.dataLayer.repositories.OperationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class FragmentAddPost : Fragment() {
    private lateinit var binding: LayoutFragmentAddPostBinding
    private val CODE_FILE_EXPLORER = 10
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val viewModelUser: ViewModelUser by activityViewModels()

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

    private fun pushImage(data: String): LiveData<String> {
        return viewModelPost.uploadImage(SerializeImage(
                imageData = data,
                extension = null
        ))
    }

    private fun pushPost(post: SerializePost) {
        viewModelPost.uploadPost(post).observe(viewLifecycleOwner, Observer {
            if (it == OperationStatus.FINISHED) {
                findNavController().popBackStack()
            }
        })
    }

    private fun uploadPost(user: User) {
        //get image data
        val drawable = binding.postImageAdd.drawable

        if (drawable != null) {

            lifecycleScope.launch(Dispatchers.Main) {

                pushImage(drawable.toBase64()).observe(viewLifecycleOwner, Observer {
                    if (!it.isNullOrEmpty()) {
                        val post = SerializePost(
                                title = binding.postTitleAdd.text.toString(),
                                content = binding.postContentAdd.text.toString(),
                                userID = user.userID,
                                image = it
                        )
                        pushPost(post)
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

