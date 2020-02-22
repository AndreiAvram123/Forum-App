package com.example.bookapp;

import android.os.Build;

import com.example.bookapp.activities.WelcomeActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class SimpleTests {
    private WelcomeActivity welcomeActivity;

    @Before
    public void setUp() {
        welcomeActivity = Robolectric.buildActivity(WelcomeActivity.class)
                .create()
                .get();
    }





}
