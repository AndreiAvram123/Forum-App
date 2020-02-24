package com.example.bookapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapters.AdapterRecipesData;
import com.example.bookapp.R;
import com.example.bookapp.databinding.LayoutFragmentPostsDataBinding;
import com.example.bookapp.models.Post;

import java.util.ArrayList;

public class PostsDataFragment extends Fragment {

    private static final String KEY_DATA = "KEY_DATA";
    private ArrayList<Post> data;
    private AdapterRecipesData adapterRecipesData;
    private LayoutFragmentPostsDataBinding binding;

    public static PostsDataFragment getInstance(@NonNull ArrayList<Post> recipes) {
        PostsDataFragment postsDataFragment = new PostsDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_DATA, recipes);
        postsDataFragment.setArguments(bundle);
        return postsDataFragment;
    }


    public void addNewSavedPost(Post post) {
        data.add(post);
        adapterRecipesData.notifyDataSetChanged();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Inflate the layout for this fragment
        this.data = getArguments().getParcelableArrayList(KEY_DATA);
        if(data ==null){
            this.data = new ArrayList<>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        if (data != null && data.size() > 0) {
            binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_posts_data, container, false);
            view = binding.getRoot();
            initializeRecyclerViewAdapter();
            bindDataToViews();
        } else {
            view = inflater.inflate(R.layout.layout_no_data, container, false);
        }
        return view;
    }

    private void bindDataToViews() {
        binding.numberResults.setText(data.size() + "");
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_parameters,
                R.layout.custom_item_spinner);
        binding.spinnerSortOptions.setAdapter(spinnerAdapter);
        binding.sortTextButton.setOnClickListener(view -> binding.spinnerSortOptions.performClick());
        //make a final boolean array in order to access it from
        //withing the inner class
        final boolean[] notFirstCall = {false};
        binding.spinnerSortOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //the onItemSelected method is called the first time when the listener is attached
                if (notFirstCall[0]) {
                    String sortCriteria = parent.getItemAtPosition(position).toString();
                    adapterRecipesData.sort(sortCriteria);
                }
                notFirstCall[0] = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * This method initialises all the the recyclerView
     * with a recyclerView adapter, a layout manager
     * and an item decoration
     */
    private void initializeRecyclerViewAdapter() {
        adapterRecipesData = new AdapterRecipesData(data, getActivity());
        RecyclerView recyclerView = binding.recyclerViewPopularBooks;
        recyclerView.setAdapter(adapterRecipesData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    public void removePost(Post post) {
        data.remove(post);
    }
}
