package com.example.bookapp.user

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import com.example.bookapp.R
import com.example.bookapp.models.User
import com.example.dataLayer.interfaces.dao.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAccountManager @Inject constructor(val sharedPreferences: SharedPreferences,
                                             val context: Context,
                                             val userDao: UserDao) {


    fun getCurrentUser(): User {
        val userID = sharedPreferences.getInt(context.getString(R.string.key_user_id), 0)
        return User(userID = userID,
                username = sharedPreferences.getStringNotNull(R.string.key_email),
                email = sharedPreferences.getStringNotNull(R.string.key_username),
                profilePicture = "")
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

    fun getString(key: Int): String {
        return context.getString(key)
    }

    suspend fun saveUserInMemory(user: User) {
        sharedPreferences.edit {
            putInt(getString(R.string.key_user_id), user.userID)
            putString(getString(R.string.key_username), user.username)
            putString(getString(R.string.key_email), user.email)
            putString(getString(R.string.key_profile_picture), user.profilePicture)
        }
        userDao.insertUser(user)
    }
}