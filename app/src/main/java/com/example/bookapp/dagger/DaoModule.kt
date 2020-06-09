package com.example.bookapp.dagger

import android.app.Application
import android.content.Context
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.interfaces.dao.RoomUserDao
import com.example.dataLayer.interfaces.dao.RoomUserDao_Impl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@Module
class DaoModule() {

    @Provides
    fun postDao(context: Context): RoomPostDao = PostDatabase.getDatabase(context).postDao()

    @Provides
    fun userDao(context: Context): RoomUserDao = PostDatabase.getDatabase(context).userDao()

}