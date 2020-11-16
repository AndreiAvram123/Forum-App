package com.andrei.kit.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.andrei.kit.R
import com.andrei.kit.models.User
import com.andrei.dataLayer.interfaces.dao.UserDao
import com.andrei.kit.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class UserAccountManager @Inject constructor(private val sharedPreferences: SharedPreferences,
                                             @ApplicationContext private val context: Context,
                                             private val userDao: UserDao) {

    val continueLoginFlow = MutableLiveData(false)



    suspend fun saveUser(user: User) {
        userDao.insertUser(user)
         CoroutineScope( Dispatchers.Main).launch {
           continueLoginFlow.postAndReset(value = true, resetTo = false)
       }
    }

    fun deleteUserFromMemory() {
        sharedPreferences.clearString(context.getString(R.string.key_user_id))
    }

}