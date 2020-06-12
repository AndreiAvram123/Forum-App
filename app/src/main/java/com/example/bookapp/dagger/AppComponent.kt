package com.example.bookapp.dagger

import android.app.Application
import android.content.Context
import com.example.bookapp.activities.MainActivity
import com.example.bookapp.models.User
import com.example.bookapp.services.MessengerService
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.PostDatabase
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Singleton
@Component(modules = [RepositoryModule::class, DaoModule::class])
interface AppComponent {

    //for field parameters you must call inject
    fun inject(viewModelPost: ViewModelPost)

    fun inject(viewModelUser: ViewModelUser)

    fun inject(viewModelPost: ViewModelChat)

    fun inject(mainActivity: MainActivity)

    fun inject(messengerService: MessengerService)


    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        // "give me the context when I need it"
        fun create(@BindsInstance context: Context,
                   @BindsInstance coroutineScope: CoroutineScope,
                   @BindsInstance user: User
        ): AppComponent
    }


//    //go and make this instances
//    fun userRepo(): UserRepository
//
//    fun postRepo(): PostRepository

}