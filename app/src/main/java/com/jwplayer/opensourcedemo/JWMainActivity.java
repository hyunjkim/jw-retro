package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.client.JWAuthentication;
import com.jwplayer.opensourcedemo.client.JWRetrofitInstance;
import com.jwplayer.opensourcedemo.client.JWService;
import com.jwplayer.opensourcedemo.client.UploadVideo;
import com.jwplayer.opensourcedemo.data.JWPojo;
import com.jwplayer.opensourcedemo.network.NetworkAvailability;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JWMainActivity extends AppCompatActivity{

    private final String TAG = "JWPLAYER-LOGGER";
    private Retrofit retrofit;
    private JWService jwService;
    private Call<JWPojo> callJWAPIService;
    private TextView tv;
    final int PICK_REQUEST_CODE = 7;

    private String intentpath = "", apiFormat;

    private JWAuthentication authentication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        Button button = findViewById(R.id.gallery_btn);
        tv = findViewById(R.id.display_path_txt);

        if(networkAvailable()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(VerifyPermission.requestPermission(JWMainActivity.this)){
                        onRequestPermissionsResult(PICK_REQUEST_CODE,
                                VerifyPermission.getPermissions(),
                                VerifyPermission.getResults());
                    } else {
                        ToastUtil.toast(JWMainActivity.this, true,"Need Permission!");
                    }
                }
            });
        } else ToastUtil.toast(this, true,"NETWORK DOWN");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PICK_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    authentication = JWAuthentication.getInstance();
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("video/*");
                    startActivityForResult(openGalleryIntent, PICK_REQUEST_CODE);
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == PICK_REQUEST_CODE && resultCode!=RESULT_CANCELED && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            Uri selectedVideo = intent.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo, filePath,
                    null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            intentpath = cursor.getString(columnIndex);
            cursor.close();
            tv.setText(intentpath);
            createVideo();
        }
    }

    private void createVideo() {
        authentication.createSignature("");

        apiFormat = authentication.getApiFormat();
        String key = authentication.getApikey();
        String nonce = authentication.getApiNonce();
        String timestamp = authentication.getApiTimestamp();
        String token = authentication.getApiSignature();

        retrofit = JWRetrofitInstance.getJWRetrofitInstance("create");
        jwService = retrofit.create(JWService.class);
        callJWAPIService = jwService.createVideoToJW(apiFormat, key, nonce, timestamp ,token, key);
        callJWAPIService.enqueue(new Callback<JWPojo>() {

            @Override
            public void onResponse(Call<JWPojo> call, Response<JWPojo> response) {
                JWPojo mPojo = response.body();

                Log.i(TAG, "SUCCESS MESSAGE:" + response.message());

                if (response.isSuccessful()) UploadVideo.uploadVideo(JWMainActivity.this, retrofit, jwService, callJWAPIService, apiFormat, authentication.getAuthentication(), intentpath, mPojo);

                ToastUtil.toast(JWMainActivity.this,true,null);
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {
                Log.i(TAG, "FAILED to create a video! " + t.getMessage(), t);
                ToastUtil.toast(JWMainActivity.this,false,"FAILED to create a video!");
            }
        });

    }
    private boolean networkAvailable() {
        return NetworkAvailability.checkNetwork(getApplicationContext());
    }


}
