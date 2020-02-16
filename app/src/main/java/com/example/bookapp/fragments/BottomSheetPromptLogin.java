package com.example.bookapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
public class BottomSheetPromptLogin extends BottomSheetDialogFragment
        {

    public static final String TAG = "ActionBottomDialog";
    private BottomSheetInterface mListener;

    public static BottomSheetPromptLogin newInstance() {
        return new BottomSheetPromptLogin();
    }
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout layout1 = view.findViewById(R.id.login_with_google_item);
        layout1.setOnClickListener(layout->{
            dismiss();
            mListener.onBottomSheetItemClicked(layout1.getId());
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetInterface) {
            mListener = (BottomSheetInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface BottomSheetInterface {
        void onBottomSheetItemClicked(int itemId);
    }
}