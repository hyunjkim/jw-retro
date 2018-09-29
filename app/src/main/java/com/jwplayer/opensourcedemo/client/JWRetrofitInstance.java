package com.jwplayer.opensourcedemo.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JWRetrofitInstance {

    private static Retrofit retrofit;
    public static final String BASE_URL_UPLOAD = "http://upload.jwplatform.com";
    public static final String BASE_URL_CREATE = "https://api.jwplatform.com";

    public static Retrofit getJWRetrofitInstance(String call){

        if(retrofit == null){
            switch(call) {
                case "upload":
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL_UPLOAD)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    return retrofit;
                case "create":
                        retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_CREATE)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                    return retrofit;
            }
        }
        return retrofit;
    }

}
