package com.jwplayer.opensourcedemo.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class JWRetrofitInstance {

    private static final String BASE_URL_S3 = "http://";

    static Retrofit getJWRetrofitInstance(String address){

        Retrofit retrofit;
        address = BASE_URL_S3 + address;
                retrofit = new Retrofit.Builder()
                        .baseUrl(address)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        return retrofit;
    }

}
