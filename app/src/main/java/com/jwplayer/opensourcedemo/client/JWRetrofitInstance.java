package com.jwplayer.opensourcedemo.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JWRetrofitInstance {

    private static final String BASE_URL_UPLOAD = "http://upload.jwplatform.com";
    private static final String BASE_URL_CREATE = "https://api.jwplatform.com";

    public static Retrofit getJWRetrofitInstance(String call){

        Retrofit retrofit;

        switch(call) {
            case "create":
                    retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CREATE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                break;
            default:
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_UPLOAD)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                break;
        }
        return retrofit;
    }

}
