package com.example.bookapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.bookapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home_activity);
        Button button = findViewById(R.id.test_button);
        button.setOnClickListener(view->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
