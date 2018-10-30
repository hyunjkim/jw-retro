package com.jwplayer.opensourcedemo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkAvailability {

    private static NetworkInfo activeNetwork;

    public static boolean checkNetwork(Context applicationContext) {

        ConnectivityManager cm = (ConnectivityManager) applicationContext
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
