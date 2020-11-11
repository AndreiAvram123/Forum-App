package com.andrei.kit.utils

import android.content.SharedPreferences


 fun SharedPreferences.getStringNotNull(key: String
): String {
    val value = getString(key, "unknown")
    value?.let { return it }
    return "Unknown"
}


 fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

fun SharedPreferences.clearString(key:String){
    edit {
        putString(key,null)
    }
}
