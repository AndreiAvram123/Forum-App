package com.andrei.kit.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrei.kit.Adapters.SuggestionsAdapter
import com.andrei.kit.R
import com.andrei.kit.databinding.FragmentSearchBinding
import com.andrei.kit.models.User
import com.andrei.kit.utils.isNotConnected
import com.andrei.kit.utils.reObserve
import com.andrei.kit.viewModels.ViewModelAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(){
    private val suggestionsAdapter: SuggestionsAdapter by lazy {
        SuggestionsAdapter()
    }
    private lateinit var binding: FragmentSearchBinding;
    private val viewModelAuth: ViewModelAuth by activityViewModels()

    @Inject
    lateinit var user: User

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (connectivityManager.isNotConnected()) {
            val action = SearchFragmentDirections.actionGlobalNoInternetFragment()
            findNavController().navigate(action)
        }
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        configureRecyclerView()
        configureSearch()

        viewModelAuth.searchSuggestions.reObserve(viewLifecycleOwner, {
            suggestionsAdapter.data = ArrayList(it)
        })

        return binding.root
    }

    private fun configureRecyclerView() {
       binding.recyclerViewSearchResults.apply {
            adapter = suggestionsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }


    private fun configureSearch() {

        binding.searchView.setOnClickListener {
            binding.searchView.apply {
                if (isIconified) {
                    background =  ContextCompat.getDrawable(requireContext(),R.drawable.search_background_highlighted)
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
                        suggestionsAdapter.data.clear()
                    }
                    return false
                }
            })
        }
    }

}

