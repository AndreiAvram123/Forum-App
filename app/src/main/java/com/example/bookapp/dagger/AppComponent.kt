package com.example.bookapp.dagger

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bookapp.activities.MainActivity
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.repositories.PostRepository
import com.example.dataLayer.repositories.UserRepository
import dagger.Component
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {

    //for field parameters you must call inject
    fun inject(viewModelPost: ViewModelPost)

    fun inject(viewModelPost: ViewModelUser)

    //go and make this instances
    fun userRepo(): UserRepository

    fun postRepo(): PostRepository

    fun context(): Context
}