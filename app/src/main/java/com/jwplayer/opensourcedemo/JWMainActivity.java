package com.jwplayer.opensourcedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jwplayer.opensourcedemo.network.NetworkAvailability;
import com.jwplayer.opensourcedemo.retrofit.JWResponse;

import retrofit2.Retrofit;

public class JWMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(networkAvailable()) callJWRetrofit();
        else Toast.makeText(this, "NETWORK DOWN", Toast.LENGTH_SHORT).show();

    }

    private void callJWRetrofit() {

        final String API_URL = "http://jsjrobotics.nyc/";
        final String API_VIDEO_UPLOAD = "upload.jwplatform.com";

        Retrofit retrofit;
        JWResponse jwResponse;
        List<JWPlayerInfo> animalInfo;
        View view;

    }

    private boolean networkAvailable() {
        return NetworkAvailability.checkNetwork(getApplicationContext());
    }
}
