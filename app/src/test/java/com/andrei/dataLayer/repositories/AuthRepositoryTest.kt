package com.andrei.dataLayer.repositories

import android.os.Build
import com.andrei.TestUtilities
import com.andrei.dataLayer.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)

class AuthRepositoryTest {
    private lateinit var repo: AuthRepositoryInterface

    @Before
    fun initialize() {
        repo = TestUtilities.retrofit.create(AuthRepositoryInterface::class.java)

    }

    @Test
    fun shouldReturnNotNullSearchSuggestions()= runBlocking {
        val query = "av"
        val fetchedData = repo.fetchSuggestions(query)
        assertNotNull(fetchedData)
    }

}