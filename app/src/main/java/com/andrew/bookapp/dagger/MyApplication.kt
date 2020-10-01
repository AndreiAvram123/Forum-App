package com.andrew.bookapp.dagger

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@HiltAndroidApp
class MyApplication : Application() {
    // Instance of the AppComponent that will be used by all the Activities in the project
    // Factory to create instances of the AppComponent
    // Instance of the AppComponent that will be used by all the Activities in the project
    override fun onCreate() {
        super.onCreate()
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
            Amplify.Auth.signUp(
                    "username",
                    "Password123",
                    AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "avramandreitiberiu@gmail.com").build(),
                    { result -> Log.i("AuthQuickStart", "Result: $result") },
                    { error -> Log.e("AuthQuickStart", "Sign up failed", error) })
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }
}