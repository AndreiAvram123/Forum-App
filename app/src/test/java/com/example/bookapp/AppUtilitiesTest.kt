package com.example.bookapp

import android.os.Build
import com.example.bookapp.activities.WelcomeActivity
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@InternalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class AppUtilitiesTest {
    private lateinit var activity: WelcomeActivity

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(WelcomeActivity::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun shouldConvertFromDrawableToUri() {
        print(activity.getDrawable(R.drawable.placeholder).toString())
    }
}