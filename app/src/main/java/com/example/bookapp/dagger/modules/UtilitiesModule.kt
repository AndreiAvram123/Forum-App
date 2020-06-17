package com.example.bookapp.dagger.modules

import android.content.Context
import com.example.bookapp.R
import com.example.bookapp.user.UserAccountManager
import dagger.Module
import dagger.Provides

@Module
class UtilitiesModule {

    @Provides
    fun preferences(context: Context) = context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)

    @Provides
    fun user(userAccountManager: UserAccountManager) = userAccountManager.getCurrentUser()
}
