package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookapp.Adapters.RecyclerViewAdapterPosts;
import com.example.bookapp.R;
import com.example.bookapp.databinding.FragmentMyPostsBinding;
import com.example.bookapp.models.Post;
import com.example.bookapp.models.User;
import com.example.bookapp.viewModels.ViewModelPost;
import com.example.bookapp.viewModels.ViewModelUser;

import java.util.ArrayList;

public class FragmentMyPosts extends Fragment {

    private FragmentMyPostsBinding binding;
    private RecyclerViewAdapterPosts recyclerViewAdapterPosts;
    private ViewModelPost viewModelPost;
    private ViewModelUser viewModelUser;
    private ArrayList<Post> myPosts;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeViews(inflater, container);

        // Inflate the layout for this fragment
        if (viewModelUser == null) {
            attachObserver();
        }

        return binding.getRoot();
    }

    private void initializeViews(@NonNull LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_posts, container, false);
        initializeRecyclerView();
        initializeRecyclerViewAdapter();
    }

    private void initializeRecyclerViewAdapter() {
        if (recyclerViewAdapterPosts == null) {
            recyclerViewAdapterPosts = new RecyclerViewAdapterPosts();
        }
        binding.recyclerViewMyPosts.setAdapter(recyclerViewAdapterPosts);
    }

    private void attachObserver() {
        viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);
        User user = viewModelUser.getUser().getValue();
        if (user != null) {
            viewModelPost.getMyPosts().observe(getViewLifecycleOwner(),
                    myPosts -> recyclerViewAdapterPosts.addData(myPosts));
        }
    }

    private void initializeRecyclerView() {
        binding.recyclerViewMyPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewMyPosts.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewMyPosts.setHasFixedSize(true);
    }
}
