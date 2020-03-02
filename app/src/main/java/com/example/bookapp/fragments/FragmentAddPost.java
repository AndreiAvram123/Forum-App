package com.example.bookapp.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentAddPostBinding;
import com.example.bookapp.interfaces.MainActivityInterface;
import com.example.bookapp.models.NonUploadedPostBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class FragmentAddPost extends Fragment {
    private MainActivityInterface mainActivityInterface;
    private LayoutFragmentAddPostBinding binding;
    private static final int CODE_FILE_EXPLORER = 10;
    private String imageUri;

    public static FragmentAddPost getInstance() {
        FragmentAddPost fragmentAddPost = new FragmentAddPost();
        return fragmentAddPost;
    }

    public FragmentAddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_add_post, container, false);
        configureViews();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityInterface = (MainActivityInterface) getActivity();
    }

    private void configureViews() {
        binding.postImageAdd.setOnClickListener(view -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("image/*");
            startActivityForResult(fileIntent, CODE_FILE_EXPLORER);
        });
        binding.submitPostButton.setOnClickListener((view) -> {
            startUploadPost();
        });

    }

    private void startUploadPost() {
        NonUploadedPostBuilder nonUploadedPostBuilder = new NonUploadedPostBuilder();
        nonUploadedPostBuilder.setPostTitle(binding.postTitleAdd.getText().toString())
                .setPostCategory("Missions")
                .setPostContent(binding.postContentAdd.getEditText().getText().toString())
                .setImageName(getImageName())
                .setImageBytes(getImageBytes());
        mainActivityInterface.uploadPost(nonUploadedPostBuilder.createPostUnderUploadModel());
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_FILE_EXPLORER) {
            assert data != null;
            Uri path = data.getData();
            if (path != null) {
                binding.postImageAdd.setImageURI(path);
                imageUri = path.getPath();
            }

        }

    }

    private byte[] getImageBytes() {
        Bitmap bitmap = ((BitmapDrawable) binding.postImageAdd.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String getImageName() {
        String fileNameSegments[] = imageUri.split("/");
        String fileName = fileNameSegments[fileNameSegments.length - 1] ;
        return fileName;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mainActivityInterface = (MainActivityInterface) getActivity();
    }
}
