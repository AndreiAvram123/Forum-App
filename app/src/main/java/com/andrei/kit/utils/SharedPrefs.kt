package com.andrei.kit.utils

import android.content.SharedPreferences



 fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

fun SharedPreferences.clearString(key:String){
    edit {
        remove(key)
    }
}
