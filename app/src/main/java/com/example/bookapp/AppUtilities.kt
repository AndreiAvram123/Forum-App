package com.example.bookapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Base64
import android.util.DisplayMetrics
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream


object AppUtilities {

//todo
    //inject here activity
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://www.andreiram.co.uk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getScreenWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.isDefaultNetworkActive
    }

    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     *
     * @param email
     * @return
     */
    fun isEmailValid(email: String): Boolean {
        val regex = "[a-zA-Z0-9]+@[a-z]+\\.[a-z]+"
        return email.matches(regex.toRegex())
    }

    fun convertFromUriToDrawable(path: Uri, context: Context): Drawable {

        val inputStream: InputStream? = context.contentResolver.openInputStream(path)
        return Drawable.createFromStream(inputStream, path.toString())

    }

    suspend fun getBase64ImageFromDrawable(drawable: Drawable): String {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}