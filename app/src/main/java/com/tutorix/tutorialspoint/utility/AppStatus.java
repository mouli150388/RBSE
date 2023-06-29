package com.tutorix.tutorialspoint.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class AppStatus {

    @SuppressLint("StaticFieldLeak")
    private final static AppStatus instance = new AppStatus();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    NetworkInfo wifiInfo, mobileInfo;
    private boolean connected = false;

    public static AppStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            return connected;


        } catch (Exception e) {


        }
        return connected;
    }
}