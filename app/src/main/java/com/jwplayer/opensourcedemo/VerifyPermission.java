package com.jwplayer.opensourcedemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class VerifyPermission {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static int writePermission, readPermission;

    /**
     * Help from : https://stackoverflow.com/questions/33030933/android-6-0-open-failed-eacces-permission-denied#answer-37038313
     * */
    public static boolean requestPermission(Activity applicationContext) {

        // Check if we have read or write permission
        writePermission = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        readPermission = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            // We don't have permission so prompt the user
            print("writePermission",writePermission);
            print("readPermission",readPermission);
            ActivityCompat.requestPermissions(
                    applicationContext,
                    PERMISSIONS_STORAGE,
                    granted
            );
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    applicationContext,
                    PERMISSIONS_STORAGE,
                    granted
            );
        }
        return writePermission == granted && readPermission == granted;
    }

    public static int[] getResults() {
        return new int[] {writePermission,readPermission} ;
    }

    public static String[] getPermissions() {
        return new String[] {"write","read"};
    }

    private static void print(String s, int n){
        Log.i("JWPLAYER-LOGGER",s+ "Permissions: "+n);
    }

}
