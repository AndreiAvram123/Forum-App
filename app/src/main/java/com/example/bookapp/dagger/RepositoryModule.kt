package com.example.bookapp.dagger

import android.content.Context
import android.net.ConnectivityManager
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.UserRepositoryInterface
import com.example.dataLayer.repositories.PostRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("http://www.andreiram.co.uk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun getPostRepository(retrofit: Retrofit): PostRepositoryInterface = retrofit.create(PostRepositoryInterface::class.java)

    @Provides
    @Singleton
    fun getUserRepository(retrofit: Retrofit): UserRepositoryInterface = retrofit.create(UserRepositoryInterface::class.java)

    @Provides
    fun getConnectivityManager(context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}
