package com.example.bookapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.SuggestionsAdapter;
import com.example.bookapp.R;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class PostSuggestionsFragment extends Fragment {
    private static final String KEY_SUGGESTIONS_ARRAY = "KEY_SUGGESTIONS_ARRAY";
    ArrayList<Post> suggestions;
    RecyclerView recyclerView;

    public static PostSuggestionsFragment getInstance(ArrayList<Post> suggestions) {
        PostSuggestionsFragment postSuggestionsFragment = new PostSuggestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_SUGGESTIONS_ARRAY, suggestions);
        postSuggestionsFragment.setArguments(bundle);
        return postSuggestionsFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        suggestions = getArguments().getParcelableArrayList(KEY_SUGGESTIONS_ARRAY);
        if (suggestions != null && !suggestions.isEmpty()) {
            view = inflater.inflate(R.layout.layout_recycler_view, container, false);
            recyclerView = view.findViewById(R.id.recycler_view_string_data);
            initializeRecyclerView();
        } else {
            view = inflater.inflate(R.layout.layout_no_data, container, false);
            ((TextView) view.findViewById(R.id.no_data_message)).setText("No results");
        }
        return view;
    }



    private void initializeRecyclerView() {
        SuggestionsAdapter suggestionsRecyclerView = new SuggestionsAdapter(suggestions, getActivity());
        recyclerView.setAdapter(suggestionsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
