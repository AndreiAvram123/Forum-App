package com.example.bookapp.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentAddPostBinding;
import com.example.bookapp.interfaces.MainActivityInterface;

public class FragmentAddPost extends Fragment {
    private MainActivityInterface mainActivityInterface;
    private LayoutFragmentAddPostBinding layoutFragmentAddPostBinding;

    public static FragmentAddPost getInstance() {
        FragmentAddPost fragmentAddPost = new FragmentAddPost();
        return fragmentAddPost;
    }

    public FragmentAddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutFragmentAddPostBinding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_add_post, container, false);
        configureViews();
        return layoutFragmentAddPostBinding.getRoot();
    }

    private void configureViews() {
        layoutFragmentAddPostBinding.openFileExplorerButton.setOnClickListener(view -> {
            Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
            fileintent.setType("file/*");
            startActivityForResult(fileintent, 2);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mainActivityInterface = (MainActivityInterface) getActivity();
    }
}
