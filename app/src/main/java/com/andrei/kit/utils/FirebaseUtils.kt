package com.andrei.kit.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

suspend fun FirebaseUser.updateProfilePicture(imageURL:String){
    val profileUpdates = userProfileChangeRequest {
        photoUri = Uri.parse(imageURL)
    }
    updateProfile(profileUpdates).await()
}
suspend fun FirebaseUser.updateUsername(username:String){
    val profileUpdates = userProfileChangeRequest {
        displayName = username
    }
    updateProfile(profileUpdates).await()
}