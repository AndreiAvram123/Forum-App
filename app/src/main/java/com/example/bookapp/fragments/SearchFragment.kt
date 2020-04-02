package com.example.bookapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.SuggestionsAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSearchBinding
import com.example.bookapp.models.Post
import com.example.bookapp.viewModels.ViewModelSearch
import java.util.*

class SearchFragment : Fragment() {
    private var searchView: SearchView? = null
    private lateinit var binding: FragmentSearchBinding
    private var suggestionsAdapter: SuggestionsAdapter = SuggestionsAdapter()
    private val viewModelSearch: ViewModelSearch by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        super.onViewCreated(container!!, savedInstanceState)
        attachObserverToViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        searchView = binding.searchView
        configureRecyclerView()
        configureSearch()
        attachObserverToViewModel()
        return binding.root
    }

    private fun configureRecyclerView() {
        binding.recyclerViewSearchResults.adapter = suggestionsAdapter
        binding.recyclerViewSearchResults.setHasFixedSize(true)
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewSearchResults.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun attachObserverToViewModel() {
        viewModelSearch.getObservableSearchSuggestions().observe(viewLifecycleOwner, Observer { suggestionsAdapter.data = it })
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
                        viewModelSearch.requestSuggestions(newQuery)
                    }
                } else {
                    suggestionsAdapter.data = ArrayList()
                }
                return false
            }
        })
    }

}