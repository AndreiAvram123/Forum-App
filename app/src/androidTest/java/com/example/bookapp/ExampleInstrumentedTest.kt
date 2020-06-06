package com.example.bookapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4


import org.junit.Test
import org.junit.runner.RunWith

import com.example.bookapp.fragments.FragmentAddPost
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    @Test
    fun notFilledFieldsShouldDisplayErrorMessage() {
        val scenario = launchFragmentInContainer<FragmentAddPost>()
        onView(withId(R.id.submit_post_button)).perform(click())

        onView(withId(R.id.error_message_add))
                .check(matches(withText(R.string.fields_not_completed)))
    }

}