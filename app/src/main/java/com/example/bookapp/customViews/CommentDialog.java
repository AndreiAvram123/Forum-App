package com.example.bookapp.customViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.bookapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class CommentDialog extends Dialog {

    private CommentDialogInterface commentDialogInterface;
    private int postID;
    public CommentDialog(@NonNull Context context,@NonNull CommentDialogInterface commentDialogInterface, int postID) {
        super(context);
        this.commentDialogInterface = commentDialogInterface;
        this.postID = postID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment_dialog);
        setCanceledOnTouchOutside(false);
        TextInputLayout commentText = findViewById(R.id.comment_text_input);
        Button submitCommentButton = findViewById(R.id.submit_comment_button);
        submitCommentButton.setOnClickListener(view -> {
            hide();
            commentDialogInterface.submitComment(commentText.getEditText().getText().toString(),postID);
        });
    }

    public interface CommentDialogInterface {
        void submitComment(String comment,int postID);
    }
}
