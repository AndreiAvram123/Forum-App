package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.Resource
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.UserRepoInterface
import com.andrei.dataLayer.interfaces.dao.UserDao
import com.andrei.dataLayer.models.UpdateProfileImageRequest
import com.andrei.kit.models.User
import com.andrei.kit.utils.addAndNotify
import com.andrei.kit.utils.isConnected
import com.andrei.kit.utils.updateProfilePicture
import com.andrei.kit.viewModels.ViewModelUser
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@ActivityScoped
class UserRepository @Inject constructor(
        private val repo:UserRepoInterface,
        private val connectivityManager: ConnectivityManager,
        private val userDao: UserDao,
        private val user:User,
        private val  coroutineScope: CoroutineScope,
        private val responseHandler: ResponseHandler
) {


    val friends :MutableLiveData<MutableList<User>> by lazy {
        MutableLiveData<MutableList<User>>().also {
            coroutineScope.launch {
                fetchFriends()
            }
        }
    }

    fun fetchUserDetails(userID:String): LiveData<User> = liveData{
        emitSource(userDao.getUser(userID))
        if(connectivityManager.isConnected()){
            try{
                val fetchedData = repo.fetchUser(userID)
                userDao.insertUser(fetchedData.toUser())
            }catch (e:Exception){
              responseHandler.handleException<User>(e,"fetch user details")
            }
        }
    }

    private suspend fun fetchFriends(){
        if(connectivityManager.isConnected()) {
            try {
               val fetchedData =  repo.fetchUserFriends(user.userID)
                val transformedData = fetchedData.map { it.toUser() }
                friends.addAndNotify(transformedData)
            }catch (e:Exception){
                responseHandler.handleException<User>(e,"fetch friends")
            }
        }
    }

     fun changeProfilePicture(imageData: String, firebaseUser: FirebaseUser) = liveData{
        emit(Resource.loading<Any>())
            try {
               val updateProfileImageRequest = UpdateProfileImageRequest(
                        imageData =  imageData,
                       userID = firebaseUser.uid
               )
                val user = repo.updateProfilePicture(updateProfileImageRequest)
                firebaseUser.updateProfilePicture(user.profilePicture)
                emit(Resource.success(Any()))
            } catch (e: Exception) {
                emit(responseHandler.handleException<Any>(e, "Change profile pictrue"))
            }
    }
}
