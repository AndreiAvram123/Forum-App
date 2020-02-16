package com.example.bookapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtilities {

    static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /**
     * This method uses a regex with the matches() method in
     * order to determine if the email address is valid or
     * not
     *
     * @param email
     * @return
     */
    public static boolean isEmailValid(String email) {
        return email.matches("[a-zA-Z0-9]+@[a-z]+\\.[a-z]+");
    }
}
