package com.example.bookapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String getDateString() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  H:m:s");
        return simpleDateFormat.format(date);
    }

    public static String convertUnixTimeToStringDate(long unixTime) {
        Date date = new Date(unixTime * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
