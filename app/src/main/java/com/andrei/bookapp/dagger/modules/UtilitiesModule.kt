package com.andrei.bookapp.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.andrei.bookapp.R
import com.andrei.bookapp.models.User
import com.andrei.dataLayer.dataMappers.toUser
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    fun user(): User = FirebaseAuth.getInstance().currentUser!!.toUser()


}
