package com.example.bookapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.bookapp.R
import com.example.bookapp.activities.WelcomeActivity
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.user.UserAccountManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var userAccountManager: UserAccountManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val logoutPreference: Preference? = findPreference("sign_out")

        logoutPreference?.setOnPreferenceClickListener {
            Intent(requireActivity(), WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.also {
                userAccountManager.deleteUserFromMemory()
                startActivity(it)
            }
            true
        }

    }

}