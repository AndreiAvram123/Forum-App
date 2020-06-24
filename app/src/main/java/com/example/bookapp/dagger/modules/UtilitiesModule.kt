package com.example.bookapp.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.bookapp.R
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.models.User
import com.example.bookapp.user.UserAccountManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@InstallIn(ActivityComponent::class)
@Module
class UtilitiesModule {

    @Provides
    fun getSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)
    }


    @Provides
    fun user(userAccountManager: UserAccountManager): User {
        return userAccountManager.user.value!!
    }
}
