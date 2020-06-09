package com.example.bookapp.dagger

import android.content.Context
import com.example.bookapp.models.User
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class RepositoryModule(private val coroutineScope: CoroutineScope, private val user: User) {

    @Provides
    @Singleton
    fun providesApplicationContext(): CoroutineScope = coroutineScope

    @Provides
    @Singleton
    fun user(): User = user

    @Provides
    fun dispatcher(): CoroutineDispatcher = Dispatchers.IO

}
