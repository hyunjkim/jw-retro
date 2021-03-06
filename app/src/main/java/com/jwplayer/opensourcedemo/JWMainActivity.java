package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jwplayer.opensourcedemo.client.CreateVideo;
import com.jwplayer.opensourcedemo.client.JWAuthentication;
import com.jwplayer.opensourcedemo.network.NetworkAvailability;

import java.util.Arrays;
import java.util.List;


public class JWMainActivity extends AppCompatActivity{

    private TextView tv, statusTV;
    private ProgressBar progressBar;
    final int PICK_REQUEST_CODE = 7;

    private String intentpath = "";

    private JWAuthentication authentication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        Button button = findViewById(R.id.gallery_btn);
        tv = findViewById(R.id.display_path_txt);
        statusTV = findViewById(R.id.status_tv);
        progressBar = findViewById(R.id.progressbar);
        endProgress();

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

    /*
     * @NonNull Denotes that a parameter, field or method return value can never be null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

        if (requestCode == PICK_REQUEST_CODE && resultCode!= RESULT_CANCELED && resultCode == RESULT_OK && intent != null) {

            getMediaPath(intent);

            tv.setText(intentpath);

            String mediaType = MimeTypeMap.getFileExtensionFromUrl(intentpath);

            if(typeValid(mediaType)){
                CreateVideo.createVideo(this,authentication, intentpath);
            } else {
                JWLoggerUtil.log("We do not support this MediaType: "+ mediaType);
                updateStatus("We do not support this MediaType: "+ mediaType);
            }
        }
    }

    private void getMediaPath(Intent intent) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Uri selectedVideo = intent.getData();
        int columnIndex = 0;

        if(intent.getData() != null){
            Cursor cursor = getContentResolver().query(selectedVideo, filePath,null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                columnIndex = cursor.getColumnIndex(filePath[0]);
                intentpath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else {
            JWLoggerUtil.log("No path was found");
            updateStatus("No path was found");
        }
    }

    private boolean networkAvailable() {
        return NetworkAvailability.checkNetwork(getApplicationContext());
    }

    public void updateStatus(String status){
        statusTV.setText(status);
    }

    private boolean typeValid(String videoType){
        List<String> supportedTypes = Arrays.asList("mp4","webm","flv","aac","mp3","vorbis","m3u8","smil","mpd","rtmp","youtube");
        return supportedTypes.contains(videoType);
    }

    public void showProgress() {
        progressBar.setMax(100);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }
    public void endProgress(){
        progressBar.setVisibility(ProgressBar.GONE);
    }
}
