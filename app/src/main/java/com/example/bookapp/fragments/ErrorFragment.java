package com.example.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookapp.R;


public class ErrorFragment extends Fragment {

    private static final String KEY_ERROR_MESSAGE = "KEY_ERROR_MESSAGE";
    private static final String KEY_ICON_ID = "KEY_ICON_ID";
    private ErrorFragmentInterface errorFragmentInterface;
    private TextView errorTextView;
    private ImageView errorImageView;
    private Button refreshErrorStateButton;
    private String errorMessage;


    public static ErrorFragment getInstance(@NonNull String errorMessage, @Nullable int iconId) {
        ErrorFragment errorFragment = new ErrorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ERROR_MESSAGE, errorMessage);
        bundle.putInt(KEY_ICON_ID, iconId);
        errorFragment.setArguments(bundle);
        return errorFragment;
    }



    public ErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.layout_fragment_error, container, false);
        if(getActivity() instanceof ErrorFragmentInterface) {
            errorFragmentInterface = (ErrorFragmentInterface) getActivity();
        }
        initializeViews(layout);
        bindDataToViews();

        return layout;
    }

    private void initializeViews(View layout) {
        errorTextView = layout.findViewById(R.id.text_error_fragment);
        errorImageView = layout.findViewById(R.id.image_error_fragment);
        refreshErrorStateButton = layout.findViewById(R.id.button_refresh_error_state);
    }

    private void bindDataToViews() {
        if (getArguments() != null) {
            errorMessage = getArguments().getString(KEY_ERROR_MESSAGE);
            errorTextView.setText(errorMessage);
            int iconResourceId = getArguments().getInt(KEY_ICON_ID);
            if (iconResourceId != 0) {
                errorImageView.setImageResource(iconResourceId);

            refreshErrorStateButton.setOnClickListener(view->{
                if(errorFragmentInterface!=null){
                    errorFragmentInterface.refreshErrorState(errorMessage);
                }
            });
            }
        }
    }


    public interface ErrorFragmentInterface {
        void refreshErrorState(String error);
    }
}
