package com.andrei.dataLayer.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.kit.user.UserAccountManager
import com.andrei.dataLayer.dataMappers.UserMapper
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.AuthRepositoryInterface
import com.andrei.dataLayer.models.serialization.AuthenticationData
import com.andrei.dataLayer.models.serialization.RegisterUserDTO
import com.andrei.kit.R
import com.andrei.kit.utils.updateProfilePicture
import com.andrei.kit.utils.updateUsername
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ActivityScoped
class AuthRepository @Inject constructor(private val repo: AuthRepositoryInterface,
                                         private val userAccountManager: UserAccountManager,
                                         private val coroutineScope: CoroutineScope,
                                         private val sessionSettingsRepository: SessionSettingsRepository,
                                         private val responseHandler:ResponseHandler,
                                         @ApplicationContext private val context: Context) {

    val authenticationError = MutableLiveData<String>()
    val registrationError = MutableLiveData<Resource<String>>()

    private val auth = FirebaseAuth.getInstance()


     suspend fun loginWithGoogle(googleSignInAccount: GoogleSignInAccount)  {
        val credential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken,null)
        try {
           val userFirebase = auth.signInWithCredential(credential).await().user
           userFirebase?.let { firebaseUser ->
               val response = repo.getUserFomUID(firebaseUser.uid)
                    if (response.authenticationData !=null) {
                        saveAuthenticationData(response.authenticationData)
                   }else{
                        registerGoogleUserToApi(firebaseUser)?.let{saveAuthenticationData(it)}
                    }
            }

        }catch (e:Exception){
            responseHandler.handleException<Any>(e,"Login with google")
            authenticationError.postValue(context.getString(R.string.unknown_error))
        }
    }


     fun registerWithUsernameAndPassword(username: String, email: String, password: String)  {
             auth.createUserWithEmailAndPassword( email, password).addOnSuccessListener {
                val firebaseUser = it.user
                if (firebaseUser != null) {
                    coroutineScope.launch {
                        try {
                            val authenticationData = registerUserToApi(firebaseUser, username)
                            if(authenticationData!=null)
                                saveAuthenticationData(authenticationData)
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


    private suspend fun registerUserToApi(firebaseUser: FirebaseUser,username: String) :AuthenticationData?{
       val email = firebaseUser.email
        if(email !=null) {
            val registerUserDTO = RegisterUserDTO(uid = firebaseUser.uid,
                    displayName = username,
                    email = email
            )
            try{
            val response = repo.register(registerUserDTO)
            val authenticationData = response.authenticationData
            authenticationData?.let {
                firebaseUser.updateProfilePicture(it.userDTO.profilePicture)
                firebaseUser.updateUsername(username)
                return it
            }
        }catch (e:Exception){
            responseHandler.handleException<Any>(e,"Register user to api")
         }
        }
        return null
    }

    private suspend fun registerGoogleUserToApi(firebaseUser: FirebaseUser) : AuthenticationData?{
        val displayName = firebaseUser.displayName
        val email = firebaseUser.email
        check(displayName!=null){"Display name cannot be null"}
        check(email != null){"email cannot be null"}
            try {
                val registerUserDTO = RegisterUserDTO(uid = firebaseUser.uid,
                        displayName = displayName,
                        email = email)
               val response =  repo.register(registerUserDTO)
               val authenticationData = response.authenticationData
                authenticationData?.let{
                    firebaseUser.updateProfilePicture(it.userDTO.profilePicture)
                    return it
                }
            }catch (e:Exception){
                responseHandler.handleException<Any>(e,"Register google user to api")
            }
     return null
    }

    private suspend fun saveAuthenticationData(data: AuthenticationData){
             sessionSettingsRepository.accessToken = data.token
             userAccountManager.saveUser(data.userDTO.toUser())

    }

    fun fetchSearchSuggestions(query: String) = liveData {
            try {
                val fetchedSuggestions = repo.fetchSuggestions(query)
                emit(fetchedSuggestions.map { UserMapper.mapToDomainObject(it) })
            } catch (e: Exception) {
                responseHandler.handleException<Any>(e,"fetching user search suggestions")
            }
    }

    suspend fun login(email: String, password: String) {
        try {
             val user = auth.signInWithEmailAndPassword(email, password).await().user
              if(user !=null){
                val response = repo.getUserFomUID(user.uid)
                  response.authenticationData?.let { saveAuthenticationData(it) }
             }
        }catch (e :Exception)
        {
             authenticationError.postValue(e.message ?: context.getString(R.string.unknown_error))
        }
    }

}