package com.jwplayer.opensourcedemo.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void toast(Context context, boolean toastIs, String message){
        if(message!=null){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            if (toastIs)Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
            else Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
        }
    }
}
