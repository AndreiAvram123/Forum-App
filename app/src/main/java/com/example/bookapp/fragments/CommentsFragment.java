package com.example.bookapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookapp.Adapters.AdapterComments;
import com.example.bookapp.R;
import com.example.bookapp.models.Comment;

import java.util.ArrayList;

public class CommentsFragment extends Fragment {
    private static final String KEY_COMMENTS = "KEY_COMMENTS";
    private ArrayList<Comment> comments;
    private AdapterComments adapterComments;

    public static CommentsFragment getInstance(ArrayList<Comment> comments) {
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_COMMENTS, comments);
        commentsFragment.setArguments(bundle);
        return commentsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.layout_fragment_comments, container, false);
        comments = getArguments().getParcelableArrayList(KEY_COMMENTS);

        if (comments != null) {
            initializeRecyclerView(layout.findViewById(R.id.recycler_comments));
        }
        return layout;
    }

    void addComment(@NonNull Comment comment) {
        comments.add(comment);
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        adapterComments = new AdapterComments(comments);
        recyclerView.setAdapter(adapterComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

}
