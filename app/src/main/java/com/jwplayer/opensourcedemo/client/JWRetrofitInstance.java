package com.jwplayer.opensourcedemo.client;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JWRetrofitInstance {

    private static Retrofit retrofit;
    public static final String BASE_URL_UPLOAD = "http://upload.jwplatform.com";
    public static final String BASE_URL_CREATE = "https://api.jwplatform.com";

    public static Retrofit getJWRetrofitInstance(String call){
        switch(call) {
            case "upload":
                Log.i("JWPLAYER-LOGGER", "retrofit :" + BASE_URL_UPLOAD);
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_UPLOAD)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                break;
            case "create":
                    retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CREATE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                break;
        }
        return retrofit;
    }

}
