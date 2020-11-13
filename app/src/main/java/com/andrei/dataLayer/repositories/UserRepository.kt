package com.andrei.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.ResponseHandler
import com.andrei.dataLayer.interfaces.UserRepoInterface
import com.andrei.dataLayer.interfaces.dao.UserDao
import com.andrei.kit.models.User
import com.andrei.kit.utils.addAndNotify
import com.andrei.kit.utils.isConnected
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
        private val repo:UserRepoInterface,
        private val connectivityManager: ConnectivityManager,
        private val userDao: UserDao,
       private val user:User,
        private val  coroutineScope: CoroutineScope
) {
    private val responseHandler = ResponseHandler()


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
}
