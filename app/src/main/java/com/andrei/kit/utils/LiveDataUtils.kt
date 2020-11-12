package com.andrei.kit.utils

import android.net.ConnectivityManager
import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            if(t!=null){
                removeObserver(this)
            }
        }
    })
}


fun <T> MutableLiveData<T>.postAndReset(value: T, resetTo: T? = null) {
    postValue(value)
    Handler().post { postValue(resetTo) }
}

fun <T> LiveData<T>.observeRequest(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        var observed = 0
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            if(observed == 2 ){
                removeObserver(this)
            }
        }
    })
}
fun ConnectivityManager.isConnected() = this.activeNetwork != null