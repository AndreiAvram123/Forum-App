package com.example.bookapp.dagger

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@HiltAndroidApp
class MyApplication : Application() {
    // Instance of the AppComponent that will be used by all the Activities in the project
    // Factory to create instances of the AppComponent
    // Instance of the AppComponent that will be used by all the Activities in the project


}