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
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.models.serialization.SerializeFriendRequest
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SearchFragment : Fragment(), SuggestionsAdapter.Callback {
    private var searchView: SearchView? = null
    private val suggestionsAdapter: SuggestionsAdapter = SuggestionsAdapter(this)
    private lateinit var binding: FragmentSearchBinding;
    private val viewModelUser: ViewModelUser by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onViewCreated(container!!, savedInstanceState)

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchView = binding.searchView
        configureRecyclerView()
        configureSearch()
        viewModelUser.searchSuggestions.observe(viewLifecycleOwner, Observer {
            suggestionsAdapter.data = ArrayList(it)
        })


        return binding.root
    }

    private fun configureRecyclerView() {
        binding.recyclerViewSearchResults.adapter = suggestionsAdapter
        binding.recyclerViewSearchResults.setHasFixedSize(true)
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearchResults.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun clearSearch() {
        searchView!!.onActionViewCollapsed()
        searchView!!.background = requireActivity().getDrawable(R.drawable.search_background)
    }

    private fun configureSearch() {
        searchView!!.setOnClickListener {
            if (searchView!!.isIconified) {
                searchView!!.background = requireActivity().getDrawable(R.drawable.search_background_highlighted)
                searchView!!.isIconified = false
            }
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    override fun sendFriendRequest(receiver: User) {
        val user = viewModelUser.user.value
        if (user != null) {
            val friendRequest = SerializeFriendRequest(senderID = user.userID, receiverID = receiver.userID)
            viewModelUser.sendFriendRequest(friendRequest)
        }
    }
}

