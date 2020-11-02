package com.andrei.bookapp.dagger.modules

import android.content.Context
import android.net.ConnectivityManager
import com.andrei.dataLayer.engineUtils.AuthInterceptor
import com.andrei.bookapp.user.UserAccountManager
import com.andrei.dataLayer.interfaces.ChatRepositoryInterface
import com.andrei.dataLayer.interfaces.CommentRepoInterface
import com.andrei.dataLayer.interfaces.PostRepositoryInterface
import com.andrei.dataLayer.interfaces.UserRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InternalCoroutinesApi
@InstallIn(ActivityComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl("http://www.andreiram.co.uk")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun httpClient(userAccountManager: UserAccountManager,
                   connectivityManager: ConnectivityManager): OkHttpClient {
        val token = userAccountManager.getToken()
        val interceptor = AuthInterceptor(token,connectivityManager)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun getPostRepository(retrofit: Retrofit): PostRepositoryInterface = retrofit.create(PostRepositoryInterface::class.java)

    @Provides
    fun getUserRepository(retrofit: Retrofit): UserRepositoryInterface = retrofit.create(UserRepositoryInterface::class.java)

    @Provides
    fun getChatRepository(retrofit: Retrofit): ChatRepositoryInterface = retrofit.create(ChatRepositoryInterface::class.java)

    @Provides
    fun getCommentsRepository(retrofit: Retrofit): CommentRepoInterface = retrofit.create(CommentRepoInterface::class.java)


    @Provides
    fun getConnectivityManager(@ApplicationContext context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun coroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

}
