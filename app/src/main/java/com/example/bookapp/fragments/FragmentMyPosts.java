package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookapp.Adapters.RecyclerViewAdapterPosts;
import com.example.bookapp.R;
import com.example.bookapp.databinding.FragmentMyPostsBinding;
import com.example.bookapp.viewModels.ViewModelPost;

public class FragmentMyPosts extends Fragment {

    private FragmentMyPostsBinding binding;
    private RecyclerViewAdapterPosts recyclerViewAdapterPosts;
    private ViewModelPost viewModelPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewModelPost == null) {
            attachObserver();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_posts, container, false);
        initializeRecyclerViewAdapter();
        initializeRecyclerView();
        return binding.getRoot();
    }

    private void initializeRecyclerViewAdapter() {
        if (viewModelPost.getSavedPosts().getValue() != null) {
            recyclerViewAdapterPosts = new RecyclerViewAdapterPosts(viewModelPost.getSavedPosts().getValue(), requireActivity());
            binding.recyclerViewMyPosts.setAdapter(recyclerViewAdapterPosts);
        }
    }

    private void attachObserver() {
        viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelPost.getMyPosts().observe(getViewLifecycleOwner(), myPosts -> recyclerViewAdapterPosts.setData(myPosts));
    }

    private void initializeRecyclerView() {
        binding.recyclerViewMyPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewMyPosts.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewMyPosts.setHasFixedSize(true);
    }
}
