package com.example.bookapp.dagger.modules

import android.content.Context
import com.example.bookapp.R
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.dao.ChatDao
import com.example.dataLayer.interfaces.dao.MessageDao
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.interfaces.dao.UserDao
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Module
class DaoModule() {

    @Provides
    @Singleton
    fun postDao(context: Context): RoomPostDao = PostDatabase.getDatabase(context).postDao()

    @Provides
    @Singleton
    fun userDao(context: Context): UserDao = PostDatabase.getDatabase(context).userDao()

    @Provides
    @Singleton
    fun messageDao(context: Context): MessageDao = PostDatabase.getDatabase(context).messageDao()

    @Provides
    @Singleton
    fun chatDao(context: Context): ChatDao = PostDatabase.getDatabase(context).chatDao()

}