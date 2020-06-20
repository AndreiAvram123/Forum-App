package com.example.bookapp.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.R
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.dao.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAccountManager @Inject constructor(private val sharedPreferences: SharedPreferences,
                                             val context: Context,
                                             val userDao: UserDao) {

    val user: MutableLiveData<User> by lazy {
        val user = User(userID = sharedPreferences.getInt(R.string.key_user_id),
                username = sharedPreferences.getStringNotNull(R.string.key_username),
                email = sharedPreferences.getStringNotNull(R.string.key_email),
                profilePicture = sharedPreferences.getStringNotNull(R.string.key_profile_picture))
        MutableLiveData<User>(user)
    }


    suspend fun saveUserInMemory(user: User) {
        persistUser(user)
        userDao.insertUser(user)
    }

    private fun persistUser(user: User) {
        sharedPreferences.edit {
            putInt(getString(R.string.key_user_id), user.userID)
            putString(getString(R.string.key_username), user.username)
            putString(getString(R.string.key_email), user.email)
            putString(getString(R.string.key_profile_picture), user.profilePicture)
        }
        this.user.postValue(user)
    }

    fun deleteUserFromMemory() {
        sharedPreferences.edit {
            putInt(getString(R.string.key_user_id), 0)
        }
    }


    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(context.getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
    }

    private fun SharedPreferences.getInt(keyID: Int
    ): Int {
        return getInt(context.getString(keyID), 0)

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

    fun getString(key: Int): String {
        return context.getString(key)
    }
}