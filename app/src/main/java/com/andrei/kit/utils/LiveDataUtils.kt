package com.andrei.kit.utils

import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}
fun <T : Any?> MutableLiveData<T>.default(initialValue: T?) = apply { setValue(initialValue) }


fun <T> MutableLiveData<T>.postAndReset(value: T, resetTo: T? = null) {
    postValue(value)
    Handler().post { postValue(resetTo) }
}