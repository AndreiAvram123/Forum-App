package com.example.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookapp.models.Message
import com.example.bookapp.models.Post
import com.example.dataLayer.RoomDao.MessageDao
import com.example.dataLayer.RoomDao.RoomPostDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Post::class, Message::class], version = 6, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): RoomPostDao
    abstract fun messageDao(): MessageDao
    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context): PostDatabase {
            val tempInstance = INSTANCE;
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                        PostDatabase::class.java,
                        "database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}