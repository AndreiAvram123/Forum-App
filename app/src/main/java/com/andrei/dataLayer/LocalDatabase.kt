package com.andrei.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andrei.kit.models.*
import com.andrei.dataLayer.interfaces.dao.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Post::class, Comment::class, User::class, Message::class, Chat::class], version = 6, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun postDao(): RoomPostDao
    abstract fun commentDao(): RoomCommentDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(application: Context): LocalDatabase {
            val tempInstance = INSTANCE;
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(application,
                        LocalDatabase::class.java,
                        "database").fallbackToDestructiveMigration()
                        .enableMultiInstanceInvalidation()
                        .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}