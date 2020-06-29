package com.socialMedia.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.socialMedia.bookapp.models.*
import com.socialMedia.dataLayer.interfaces.dao.*
import com.socialMedia.dataLayer.models.UserWithFavoritePostsCrossRef
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Post::class, Comment::class, User::class, UserWithFavoritePostsCrossRef::class, Message::class, Chat::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun postDao(): RoomPostDao
    abstract fun commentDao(): RoomCommentDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        @InternalCoroutinesApi
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