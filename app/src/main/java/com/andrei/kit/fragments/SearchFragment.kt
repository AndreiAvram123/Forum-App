package com.andrei.kit.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.SuggestionsAdapter
import com.andrei.kit.databinding.FragmentSearchBinding
import com.andrei.kit.models.User
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.kit.viewModels.ViewModelAuth
import com.andrei.dataLayer.models.serialization.SerializeFriendRequest
import com.andrei.kit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : Fragment(), SuggestionsAdapter.Callback {
    private val suggestionsAdapter: SuggestionsAdapter = SuggestionsAdapter(this)
    private lateinit var binding: FragmentSearchBinding;
    private val viewModelAuth: ViewModelAuth by activityViewModels()
    private val viewModelChat: ViewModelChat by activityViewModels()

    @Inject
    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetwork == null) {
            val action = SearchFragmentDirections.actionGlobalNoInternetFragment()
            findNavController().navigate(action)
        }
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        configureRecyclerView()
        configureSearch()
        viewModelAuth.searchSuggestions.observe(viewLifecycleOwner, Observer {
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
                    if (newQuery.trim().isNotEmpty()) {
                        viewModelAuth.searchQuery.value = newQuery
                    } else {
                        suggestionsAdapter.data = ArrayList()
                    }
                    return false
                }
            })
        }
    }

    override fun sendFriendRequest(receiver: User) {
        val friendRequest = SerializeFriendRequest(senderID = user.userID, receiverID = receiver.userID)
        viewModelChat.sendFriendRequest(friendRequest)
    }
}

