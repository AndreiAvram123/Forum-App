package com.andrew.dataLayer.repositories

import android.media.VolumeShaper
import android.net.ConnectivityManager
import androidx.lifecycle.liveData
import com.andrew.bookapp.user.UserAccountManager
import com.andrew.dataLayer.dataMappers.UserMapper
import com.andrew.dataLayer.interfaces.UserRepositoryInterface
import com.andrew.dataLayer.models.serialization.AuthenticationResponse
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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
            if (userFirebase != null) {
                    val response = repo.getUserFomUID(userFirebase.uid)
                //todo
                //use extension function
                    if (response.userDTO !=null && response.token != null) {
                        //todo
                        //break this down
                        userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(response.userDTO), response.token)
                   }else{
                        registerGoogleAccountToApi(uid = userFirebase.uid,displayName = userFirebase.displayName!!,email = userFirebase.email!!)
                    }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private suspend fun registerGoogleAccountToApi(uid: String, displayName: String, email: String) {
        try {
            val response = repo.register(uid, displayName, email)
            if (response.userDTO != null && response.token != null) {
                userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(response.userDTO), response.token)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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
                     repo.register("display name :)", user.email!!,user.uid)
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

    suspend fun register(username: String, email: String, password: String)   = liveData{
        emit(OperationStatus.ONGOING)
      try {
          val user = auth.createUserWithEmailAndPassword(email, password).await().user
          user?.let{
              repo.register(username, email,it.uid)
              emit(OperationStatus.FINISHED)
          }

      }catch (e:Exception){
          emit(OperationStatus.FAILED)
          e.printStackTrace()
      }
    }
}