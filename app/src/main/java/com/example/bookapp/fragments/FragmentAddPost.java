package com.example.bookapp.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentAddPostBinding;
import com.example.bookapp.viewModels.ViewModelPost;
import com.example.dataLayer.dataObjectsToSerialize.SerializePost;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class FragmentAddPost extends Fragment {
    private LayoutFragmentAddPostBinding binding;
    private static final int CODE_FILE_EXPLORER = 10;
    private String imageUri;
    private SerializePost.Builder builder = new SerializePost.Builder();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_add_post, container, false);
        configureViews();
        return binding.getRoot();
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
        builder.setImageBase64(Base64.encodeToString(getImageBytes(), 0));
        builder.setPostCategory("Missions");
        builder.setImageName(getImageName());
        builder.setPostTitle(binding.postTitleAdd.getText().toString());
        builder.setPostContent(binding.postContentAdd.getEditText().getText().toString());
        builder.setPostAuthorID("sdf");
        ViewModelPost viewModelPost = new ViewModelProvider(requireActivity()).get(ViewModelPost.class);
        viewModelPost.addPost(builder.build());
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
        String[] fileNameSegments = imageUri.split("/");
        return fileNameSegments[fileNameSegments.length - 1];
    }

}
