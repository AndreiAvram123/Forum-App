package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.SuggestionsAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSearchBinding
import com.example.bookapp.models.User
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SearchFragment : Fragment(), SuggestionsAdapter.Callback {
    private val suggestionsAdapter: SuggestionsAdapter = SuggestionsAdapter(this)
    private lateinit var binding: FragmentSearchBinding;
    private val viewModelUser: ViewModelUser by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        configureRecyclerView()
        configureSearch()
        viewModelUser.searchSuggestions.observe(viewLifecycleOwner, Observer {
            suggestionsAdapter.data = ArrayList(it)
        })


        return binding.root
    }

    private fun configureRecyclerView() {
        with(binding.recyclerViewSearchResults) {
            adapter = suggestionsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }


    private fun configureSearch() {

        binding.searchView.setOnClickListener {
            with(binding.searchView) {
                if (isIconified) {
                    background = requireActivity().getDrawable(R.drawable.search_background_highlighted)
                    isIconified = false
                }
            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newQuery: String): Boolean {
                    if (newQuery.trim { it <= ' ' } != "") {
                        if (newQuery.trim { it <= ' ' } != "" && newQuery.length > 1) {
                            viewModelUser.searchQuery.value = newQuery
                        }
                    } else {
                        suggestionsAdapter.data = ArrayList()
                    }
                    return false
                }
            })
        }
    }

    override fun sendFriendRequest(receiver: User) {
            val friendRequest = SerializeFriendRequest(senderID =viewModelUser.user.userID, receiverID = receiver.userID)
            viewModelChat.sendFriendRequest(friendRequest)
    }
}

