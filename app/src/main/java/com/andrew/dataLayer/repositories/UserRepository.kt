package com.andrew.dataLayer.repositories

import android.media.VolumeShaper
import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.liveData
import com.andrew.bookapp.models.User
import com.andrew.bookapp.user.UserAccountManager
import com.andrew.dataLayer.dataMappers.UserMapper
import com.andrew.dataLayer.dataMappers.toUser
import com.andrew.dataLayer.interfaces.UserRepositoryInterface
import com.andrew.dataLayer.models.serialization.AuthenticationResponse
import com.andrew.dataLayer.models.serialization.RegisterUserDTO
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(private val repo: UserRepositoryInterface,
                                         private val connectivityManager: ConnectivityManager,
                                         private val userAccountManager: UserAccountManager) {

    private val auth = FirebaseAuth.getInstance()


    suspend fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken,null)
        try {
           val userFirebase = auth.signInWithCredential(credential).await().user
           userFirebase?.let {
                    val response = repo.getUserFomUID(it.uid)
                    if (response.userDTO !=null && response.token != null) {
                        userAccountManager.saveUserAndToken(response.userDTO.toUser(), response.token)
                   }else{
                        registerUserToApi(it,null)
                    }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    suspend fun registerWithUsernameAndPassword(username: String, email: String, password: String)   = liveData{
        emit(OperationStatus.ONGOING)
        try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
            if(firebaseUser != null){
                val user = registerUserToApi(firebaseUser,username)
                user?.let {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = it.username
                        photoUri = Uri.parse(it.profilePicture)
                    }
                    firebaseUser.updateProfile(profileUpdates).await()
                    emit(OperationStatus.FINISHED)
                }
            }else{
                emit(OperationStatus.FAILED)
            }
        }catch (e:Exception){
            emit(OperationStatus.FAILED)
            e.printStackTrace()
        }
    }
    private suspend fun registerUserToApi(firebaseUser: FirebaseUser,username:String?): User? {
        val registerUserDTO = RegisterUserDTO(uid = firebaseUser.uid,
                displayName = username ?: firebaseUser.displayName!!,
                        email = firebaseUser.email!!)
        val response = repo.register(registerUserDTO)
        if(response.userDTO != null && response.token !=null) {
            val user = response.userDTO.toUser()
            userAccountManager.saveUserAndToken(user, response.token)
            return user
        }
        return null
    }

    fun fetchSearchSuggestions(query: String) = liveData {
        if (connectivityManager.activeNetwork != null) {
            try {
                val fetchedSuggestions = repo.fetchSuggestions(query)
                emit(fetchedSuggestions.map { UserMapper.mapToDomainObject(it) })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(OperationStatus.ONGOING)
        try {
             val user = auth.signInWithEmailAndPassword(email, password).await().user
              if(user !=null){
                val response = repo.getUserFomUID(user.uid)
                 if ( response.userDTO !=null && response.token !=null){
                     userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(response.userDTO),response.token)
                 }else{
                     val registerUserDTO = RegisterUserDTO(uid = user.uid,
                             displayName = "displayName",
                             email = email)
                     repo.register(registerUserDTO)
                 }
            }else{
                emit(OperationStatus.FAILED)
            }
        }catch (e :Exception)
        {
            e.printStackTrace()
            emit(OperationStatus.FAILED)
        }
    }

}