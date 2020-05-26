package com.example.bookapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppUtilities {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://139.162.192.88");
            builder.addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
        }
        return retrofit;
    }


    public static boolean isNetworkAvailable(Context context) {
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

    public static boolean isResponsePositive(JSONObject response) {
        try {
           return response.getInt("responseCode") == 200;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
