package com.example.bookapp


import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookapp.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var activity:ActivityScenario<MainActivity>

    @Before
    fun setup(){
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun shouldWork(){

    }

}