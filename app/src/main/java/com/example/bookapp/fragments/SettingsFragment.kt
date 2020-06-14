package com.example.bookapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.UserManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.bookapp.R
import com.example.bookapp.services.MessengerService
import javax.inject.Inject

class SettingsFragment @Inject constructor(val userManager: UserManager) : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val logoutPreference: Preference? = findPreference("sign_out")
        logoutPreference?.let {
            Intent(requireActivity(), MessengerService::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.also {
                startActivity(it)
            }
        }
    }
}