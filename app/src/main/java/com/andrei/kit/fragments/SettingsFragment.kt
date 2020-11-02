package com.andrei.kit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.andrei.kit.R
import com.andrei.kit.activities.WelcomeActivity
import com.andrei.kit.user.UserAccountManager
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