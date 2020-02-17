package com.example.bookapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookapp.Adapters.GridAdapter;
import com.example.bookapp.models.Recipe;

import java.util.ArrayList;

public class HomeFragment  extends Fragment {
    public static final String TAG = "TAG_HOME_FRAGMENT";
    private static final String KEY_DATA = "KEY_DATA";
    private static HomeFragment instance;
    private ArrayList<Recipe> data;

    public static HomeFragment getInstance(@NonNull ArrayList<Recipe> data){
        if(instance == null){
            instance = new HomeFragment();
        }
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(KEY_DATA,data);
        instance.setArguments(arguments);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_home_fragment,container,false);
        initializeViews(layout);
        return layout;
    }

    private void initializeViews(View layout) {
        GridView gridView = layout.findViewById(R.id.grid_view_home_fragment);
        gridView.setAdapter(new GridAdapter(data,getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){
            this.data = getArguments().getParcelableArrayList(KEY_DATA);
        }
    }
}
