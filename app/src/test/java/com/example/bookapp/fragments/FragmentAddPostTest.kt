package com.example.bookapp.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.bookapp.activities.MainActivity
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.bookapp.R
import kotlinx.android.synthetic.main.layout_fragment_add_post.view.*

@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class FragmentAddPostTest {


    @Test
    fun isFieldValidationWorking() {
        val fragment = launchFragmentInContainer<FragmentAddPost>()
      //  onView(withId(R.id.post_content_add)).perform(typeText("Steve"))
       // onView(withId(R.id.post_content_add)).check()
    }
}