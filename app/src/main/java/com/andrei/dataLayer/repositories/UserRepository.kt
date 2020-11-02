package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.liveData
import com.andrei.kit.models.User
import com.andrei.kit.user.UserAccountManager
import com.andrei.dataLayer.dataMappers.UserMapper
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.UserRepositoryInterface
import com.andrei.dataLayer.models.serialization.RegisterUserDTO
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(private val repo: UserRepositoryInterface,
                                         private val connectivityManager: ConnectivityManager,
                                         private val userAccountManager: UserAccountManager) {

    private val auth = FirebaseAuth.getInstance()
    private val responseHandler = ResponseHandler()


     fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount) = liveData {
         emit(Resource.loading())
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
               emit(Resource.success(Any()))
            }

        }catch (e:Exception){
           responseHandler.handleException<Any>(e,"login google")
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
                     emit(OperationStatus.FINISHED)
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