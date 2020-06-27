package com.example.bookapp.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.R
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.dao.UserDao
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserAccountManager @Inject constructor(private val sharedPreferences: SharedPreferences,
                                             @ApplicationContext private val context: Context,
                                             private val userDao: UserDao) {

    val user: MutableLiveData<User> by lazy {
        val user = User(userID = sharedPreferences.getInt(context.getString(R.string.key_user_id), 0),
                username = sharedPreferences.getStringNotNull(R.string.key_username),
                email = sharedPreferences.getStringNotNull(R.string.key_email),
                profilePicture = sharedPreferences.getStringNotNull(R.string.key_profile_picture))
        MutableLiveData<User>(user)
    }

    fun getToken() = sharedPreferences.getStringNotNull(R.string.key_token)

    suspend fun saveUserAndToken(user: User, token: String) {
        persistUser(user)
        persistToken(token)
        userDao.insertUser(user)
    }

    private fun persistToken(token: String) {
        sharedPreferences.edit {
            context.apply {
                putString(getString(R.string.key_token), token)
            }
        }
    }

    private fun persistUser(user: User) {
        sharedPreferences.edit {
            context.apply {
                putInt(getString(R.string.key_user_id), user.userID)
                putString(getString(R.string.key_username), user.username)
                putString(getString(R.string.key_email), user.email)
                putString(getString(R.string.key_profile_picture), user.profilePicture)
            }
        }
        this.user.postValue(user)
    }

    fun deleteUserFromMemory() {
        sharedPreferences.edit {
            context.apply {
                putInt(getString(R.string.key_user_id), 0)
                putString(getString(R.string.key_token), "")
            }
        }
    }


    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(context.getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
    }


    private fun SharedPreferences.edit(commit: Boolean = true,
                                       action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        if (commit) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

}