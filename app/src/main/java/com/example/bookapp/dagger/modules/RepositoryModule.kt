package com.example.bookapp.dagger.modules

import android.content.Context
import android.net.ConnectivityManager
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.CommentRepoInterface
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.UserRepositoryInterface
import dagger.Module
import dagger.Provides
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
    @Singleton
    fun getChatRepository(retrofit: Retrofit): ChatRepositoryInterface = retrofit.create(ChatRepositoryInterface::class.java)

    @Provides
    @Singleton
    fun getCommentsRepo(retrofit: Retrofit) = retrofit.create(CommentRepoInterface::class.java)




    @Provides
    fun getConnectivityManager(context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}
