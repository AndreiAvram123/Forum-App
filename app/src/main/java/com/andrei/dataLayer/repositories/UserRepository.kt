package com.andrei.dataLayer.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.kit.user.UserAccountManager
import com.andrei.dataLayer.dataMappers.UserMapper
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.UserRepositoryInterface
import com.andrei.dataLayer.models.serialization.AuthenticationResponse
import com.andrei.dataLayer.models.serialization.RegisterUserDTO
import com.andrei.kit.R
import com.andrei.kit.utils.postAndReset
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(private val repo: UserRepositoryInterface,
                                         private val connectivityManager: ConnectivityManager,
                                         private val userAccountManager: UserAccountManager,
                                         private val coroutineScope: CoroutineScope,
                                         @ApplicationContext private val context: Context) {

    val authenticationError = MutableLiveData<String>()
    val registrationError = MutableLiveData<Resource<String>>()

    private val auth = FirebaseAuth.getInstance()
    private val responseHandler = ResponseHandler()


     suspend fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount)  {
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
            }

        }catch (e:Exception){
            responseHandler.handleException<Any>(e,"Login with google")
            authenticationError.postValue(context.getString(R.string.unknown_error))
        }
    }


     fun registerWithUsernameAndPassword(username: String, email: String, password: String)  {
            registrationError.postValue(Resource.loading())
             auth.createUserWithEmailAndPassword( email, password).addOnSuccessListener {
                val firebaseUser = it.user
                if (firebaseUser != null) {
                    coroutineScope.launch {
                        try {
                            val response = registerUserToApi(firebaseUser, username)
                            response.authenticationData?.let { data ->
                                val user = data.userDTO.toUser()
                                updateProfilePicture(firebaseUser, user.profilePicture)
                                updateDisplayName(firebaseUser, user.username)
                                saveAuthenticationData(response)
                            }
                        }catch (e:Exception){
                            registrationError.postValue(Resource.error(context.getString(R.string.unknown_error)))
                            responseHandler.handleException<Any>(e, "Register with Username and password")
                        }
                    }
                }
            }.addOnFailureListener {
                registrationError.postValue(Resource.error(it.message ?: context.getString(R.string.unknown_error)))
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
         response.authenticationData?.let{
             userAccountManager.saveUserAndToken(token = it.token,user = it.userDTO.toUser())
         }
    }

    private fun isAuthenticated (authenticationResponse: AuthenticationResponse):Boolean{
        return authenticationResponse.authenticationData !=null
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

    suspend fun login(email: String, password: String) {
        try {
             val user = auth.signInWithEmailAndPassword(email, password).await().user
              if(user !=null){
                val response = repo.getUserFomUID(user.uid)
                 if (isAuthenticated(response)){
                     saveAuthenticationData(response)
                 }
             }
        }catch (e :Exception)
        {
             authenticationError.postValue(e.message ?: context.getString(R.string.unknown_error))
        }
    }

}