package com.andrei.dataLayer.repositories

import android.os.Build
import com.andrei.TestUtilities
import com.andrei.dataLayer.interfaces.UserRepositoryInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)

class UserRepositoryTest {
    private lateinit var repo: UserRepositoryInterface

    @Before
    fun initialize() {
        repo = TestUtilities.retrofit.create(UserRepositoryInterface::class.java)

    }

    @Test
    fun shouldReturnNotNullSearchSuggestions()= runBlocking {
        val query = "av"
        val fetchedData = repo.fetchSuggestions(query)
        assertNotNull(fetchedData)
    }

}