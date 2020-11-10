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
import com.andrei.dataLayer.models.serialization.AuthenticationResponse
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
                    if (isAuthenticated(response)) {
                        saveAuthenticationData(response)
                   }else{
                      saveAuthenticationData(registerUserToApi(it,null))
                    }
               emit(Resource.success(Any()))
            }

        }catch (e:Exception){
           emit(responseHandler.handleException<Any>(e,"login google"))
        }
    }


     fun registerWithUsernameAndPassword(username: String, email: String, password: String)   = liveData{
       emit(Resource.loading<Any>())
        try {
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
            if(firebaseUser != null){
                val response = registerUserToApi(firebaseUser,username)
                 if(response.userDTO !=null && response.token != null){
                     val user = response.userDTO.toUser()
                     updateProfilePicture(firebaseUser,user.profilePicture)
                     updateDisplayName(firebaseUser,user.username)
                     emit(responseHandler.handleSuccess(Any()))
                 }
            }
        }catch (e:Exception){
            emit(responseHandler.handleException<Any>(e,"Register with Username and password"))
        }
    }
    //todo
    //we need to change to registration response
    private suspend fun registerUserToApi(firebaseUser: FirebaseUser,username: String? = null) : AuthenticationResponse{
        val registerUserDTO = RegisterUserDTO(uid = firebaseUser.uid,
                displayName = username ?: firebaseUser.displayName!!,
                        email = firebaseUser.email!!)
        return repo.register(registerUserDTO)
    }

    private suspend fun saveAuthenticationData(response: AuthenticationResponse){
        if(response.userDTO!=null && response.token!=null) {
            userAccountManager.saveUserAndToken(response.userDTO.toUser(), response.token)
        }
    }

    private fun isAuthenticated (authenticationResponse: AuthenticationResponse):Boolean{
        return authenticationResponse.userDTO!=null && authenticationResponse.token!=null
    }

    private suspend fun updateProfilePicture(firebaseUser: FirebaseUser,profilePicture:String){
        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse(profilePicture)
        }
        firebaseUser.updateProfile(profileUpdates).await()
    }
    private suspend fun updateDisplayName(firebaseUser: FirebaseUser,newDisplayName:String){
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }
        firebaseUser.updateProfile(profileUpdates).await()
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
        emit(Resource.loading<Any>())
        try {
             val user = auth.signInWithEmailAndPassword(email, password).await().user
              if(user !=null){
                val response = repo.getUserFomUID(user.uid)
                 if (isAuthenticated(response)){
                     saveAuthenticationData(response)
                     emit(responseHandler.handleSuccess(Any()))
                 }
            }else{
                  //todo
                  //put meessage here
                emit(Resource.error<Any>("error at login"))
            }
        }catch (e :Exception)
        {
            emit(responseHandler.handleException<Any>(e,"login"))
        }
    }

}