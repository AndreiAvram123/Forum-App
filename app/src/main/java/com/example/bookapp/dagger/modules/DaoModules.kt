package com.example.bookapp.dagger.modules

import android.content.Context
import com.example.dataLayer.LocalDatabase
import com.example.dataLayer.interfaces.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@InstallIn(ActivityComponent::class)
@Module
class DaoModules {

    @Provides
    fun database(@ApplicationContext context: Context): LocalDatabase = LocalDatabase.getDatabase(context)

    @Provides
    fun postDao(database: LocalDatabase): RoomPostDao = database.postDao()

    @Provides
    fun userDao(database: LocalDatabase): UserDao = database.userDao()

    @Provides
    fun messageDao(database: LocalDatabase): MessageDao = database.messageDao()

    @Provides
    fun chatDao(database: LocalDatabase): ChatDao = database.chatDao()

    @Provides
    fun commentDao(database: LocalDatabase): RoomCommentDao = database.commentDao()

}