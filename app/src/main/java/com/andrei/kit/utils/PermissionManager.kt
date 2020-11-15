package com.andrei.kit.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionManager @Inject constructor(@ApplicationContext private val context: Context) {


    fun hasPermission (permission: String) : Boolean{
        return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED
    }
    fun isPermissionGranted(grantResults:IntArray) : Boolean {
        return grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED
    }

}
fun Fragment.requestSinglePermission(permission: String,requestCode: Int){
    val permissions = Array(1){permission}
    requestPermissions(permissions,requestCode)
}
fun Activity.requestSinglePermission(permission: String,requestCode: Int){
    val permissions = Array(1){permission}
    requestPermissions(permissions,requestCode)
}