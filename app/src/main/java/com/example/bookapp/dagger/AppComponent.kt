package com.example.bookapp.dagger

import android.content.Context
import com.example.bookapp.activities.MainActivity
import com.example.bookapp.activities.WelcomeActivity
import com.example.bookapp.dagger.modules.DaoModule
import com.example.bookapp.dagger.modules.RepositoryModule
import com.example.bookapp.dagger.modules.UtilitiesModule
import com.example.bookapp.fragments.SettingsFragment
import com.example.bookapp.models.User
import com.example.bookapp.services.MessengerService
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@InternalCoroutinesApi
@Singleton
@Component(modules = [RepositoryModule::class, DaoModule::class, UtilitiesModule::class])
interface AppComponent {

    //for field parameters you must call inject
    fun inject(viewModelPost: ViewModelPost)

    fun inject(viewModelUser: ViewModelUser)

    fun inject(viewModelPost: ViewModelChat)

    fun inject(mainActivity: MainActivity)

    fun inject(messengerService: MessengerService)
    fun inject(welcomeActivity: WelcomeActivity)

    fun inject(settingsFragment: SettingsFragment)


    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        // "give me the context when I need it"
        fun create(@BindsInstance context: Context,
                   @BindsInstance coroutineScope: CoroutineScope
        ): AppComponent
    }


}