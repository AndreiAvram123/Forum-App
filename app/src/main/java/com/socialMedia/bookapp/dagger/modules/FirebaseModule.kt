package com.socialMedia.bookapp.dagger.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
object FirebaseModule {
    @Provides
    fun getFireStore() = FirebaseFirestore.getInstance()

    @Provides
    fun getFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    fun getFirebaseAuth () = FirebaseAuth.getInstance()

}