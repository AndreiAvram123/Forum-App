package com.example.bookapp.dagger

import android.app.Application
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.interfaces.dao.RoomPostDao
import com.example.dataLayer.interfaces.dao.RoomUserDao
import com.example.dataLayer.interfaces.dao.RoomUserDao_Impl
import dagger.Module
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Module
class DaoModule(private val application: Application) {
    private val db = PostDatabase.getDatabase(application)

    fun postDao(): RoomPostDao = db.postDao()

    fun userDao(): RoomUserDao = db.userDao()

}