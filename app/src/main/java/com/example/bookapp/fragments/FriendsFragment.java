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

import com.example.bookapp.Adapters.FriendsAdapter;
import com.example.bookapp.R;
import com.example.bookapp.databinding.FragmentFriendsBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.User;
import com.example.bookapp.viewModels.ViewModelFriends;
import com.example.bookapp.viewModels.ViewModelUser;

public class FriendsFragment extends Fragment {

    private ViewModelFriends viewModelFriends;
    private FragmentFriendsBinding binding;
    private FriendsAdapter friendsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);
        //set the adapter
        configureRecyclerView();

        // Inflate the layout for this fragment
        if (viewModelFriends == null) {
            attachObservers();
        }


        return binding.getRoot();
    }

    private void configureRecyclerView() {
        if (friendsAdapter == null) {
            friendsAdapter = new FriendsAdapter((MainActivityInterface) requireActivity());
        }
        binding.recyclerViewFriends.setAdapter(friendsAdapter);
        binding.recyclerViewFriends.setHasFixedSize(true);
        binding.recyclerViewFriends.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));
        binding.recyclerViewFriends.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void attachObservers() {
        viewModelFriends = new ViewModelProvider(requireActivity()).get(ViewModelFriends.class);
        ViewModelUser viewModelUser = new ViewModelProvider(requireActivity()).get(ViewModelUser.class);

        User currentUser = viewModelUser.getUser().getValue();
        if (currentUser != null) {
            viewModelFriends.getFriends(currentUser.getUserID()).observe(getViewLifecycleOwner(), friends -> friendsAdapter.setData(friends));
        }

    }


}
