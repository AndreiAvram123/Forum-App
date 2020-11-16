package com.andrei.dataLayer.repositories

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import uk.co.coop.app.engine.settings.BaseSettingsRepository

interface SessionSettingsRepository {
    var accessToken: String?
    var tokenType: String?
    var refreshToken: String?
}

class SessionSettingsRepositoryImpl (context: Context): BaseSettingsRepository(context), SessionSettingsRepository {

    companion object {
         private const val AUTH_TOKEN_KEY = "auth_token_key"
         private const val REFRESH_TOKEN_KEY = "refresh_token_key"
         private const val TOKEN_TYPE_KEY = "token_type_key"
    }

    override var tokenType: String? by EncryptedStringDelegate(TOKEN_TYPE_KEY)
    override var accessToken: String? by EncryptedStringDelegate(AUTH_TOKEN_KEY)
    override var refreshToken: String? by EncryptedStringDelegate(REFRESH_TOKEN_KEY)

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser == null){
               clearAll()
            }
        }
    }

    private fun clearAll() {
        accessToken = null
        refreshToken = null
        tokenType = null
    }
}