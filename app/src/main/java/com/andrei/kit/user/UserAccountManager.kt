package com.andrei.kit.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.andrei.kit.R
import com.andrei.kit.models.User
import com.andrei.dataLayer.interfaces.dao.UserDao
import com.andrei.kit.utils.default
import com.andrei.kit.utils.postAndReset
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


    fun getToken() = sharedPreferences.getStringNotNull(R.string.key_token)

    val continueLoginFlow = MutableLiveData(false)



    suspend fun saveUserAndToken(user: User, token: String) {
        persistToken(token)
        userDao.insertUser(user)
         CoroutineScope( Dispatchers.Main).launch {
           continueLoginFlow.postAndReset(value = true, resetTo = false)
       }
    }

    private fun persistToken(token: String) {
        sharedPreferences.edit {
            putString(context.getString(R.string.key_token),token)
        }
    }

    fun deleteUserFromMemory() {
        sharedPreferences.edit {
            putString(context.getString(R.string.key_token),"")
        }
    }


    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(context.getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
    }


    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        editor.apply()
    }

}