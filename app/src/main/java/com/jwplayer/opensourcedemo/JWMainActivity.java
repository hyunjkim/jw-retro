package com.jwplayer.opensourcedemo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.jwplayer.opensourcedemo.client.JWAuthentication;
import com.jwplayer.opensourcedemo.client.JWRetrofitInstance;
import com.jwplayer.opensourcedemo.client.JWService;
import com.jwplayer.opensourcedemo.data.JWPojo;
import com.jwplayer.opensourcedemo.network.NetworkAvailability;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JWMainActivity extends AppCompatActivity {

    private final String TAG = "JWPLAYER-LOGGER";
    private Retrofit retrofit;
    private JWService jwService;
    private Call<JWPojo> callJWAPIService;
    private TextView tv;
    final int PICK_REQUEST_CODE = 7;

    private String intentpath = "",
            apiFormat,
            contenttype = "application/json",
            authorization,
            mimeType ;

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
                    authentication = new JWAuthentication();

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                    Uri videosUri = MediaStore.Video.Media.getContentUri("external");
                    startActivityForResult(intent, PICK_REQUEST_CODE);
                }
            });
        } else Toast.makeText(this, "NETWORK DOWN", Toast.LENGTH_SHORT).show();

    }

    /**
     * Copied from : https://stackoverflow.com/questions/20324155/get-filepath-and-filename-of-selected-gallery-image-in-android
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(requestCode == PICK_REQUEST_CODE && resultCode == RESULT_OK){
            if (intent != null) {
                intentpath = getRealPathFromURI(intent.getData());
                tv.setText(intentpath);
                createVideo();
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

                Log.i(TAG, "SUCCESS CALL URL:" + call.request().url());
                Log.i(TAG, "SUCCESS MESSAGE:" + response.message());

                if (response.isSuccessful()) uploadVideo(mPojo);

                toast(true);
                Toast.makeText(JWMainActivity.this, "SUCCESS!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {
                Log.i(TAG, "FAILED! " + t.getMessage(), t);
                toast(false);
            }
        });

    }

    /**
     *@Header("Content-Type") String contentType,
     @Header("Authorization") String authorization,
     @Part("file") RequestBody file,
     @Path("path") String path,
     @Query("api_format") String format,
     @Query("key") String video_key,
     @Query("token") String token
     * */
    private void uploadVideo(JWPojo upload){
        String key = upload.getMedia().getKey();
        String token = upload.getLink().getQuery().getToken();
        String path = upload.getLink().getPath();
        RequestBody file = getFile();

        retrofit = JWRetrofitInstance.getJWRetrofitInstance("upload");

        jwService = retrofit.create(JWService.class);

        callJWAPIService = jwService.uploadVideoToJW(contenttype,
                                                    authentication.getAuthentication(),
                                                    file,
                                                    path,
                                                    apiFormat,
                                                    key,
                                                    token);

        callJWAPIService.enqueue(new Callback<JWPojo>() {
            @Override
            public void onResponse(Call<JWPojo> call, Response<JWPojo> response) {
                toast(response.isSuccessful());
                Log.i(TAG, "UPLOAD URL: " + call.request().url());
                Log.i(TAG, "UPLOAD State: " + response.isSuccessful());
                Log.i(TAG, "UPLOAD Message: " + response.message());
                Log.i(TAG, "UPLOAD Raw: " + response.raw());
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {
                Log.i("", "FAILED To Upload! " + t.getMessage(), t);
                toast(false);
            }
        });

    }

    private RequestBody getFile(){

        File file = new File(intentpath);
        String type = MimeTypeMap.getFileExtensionFromUrl(intentpath);
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(type);
        Log.i(TAG, "FILE TYPE: " + type);
        Log.i(TAG, "FILE NAME: " + file.getAbsolutePath());

        return RequestBody.create(MediaType.parse(type),"content://"+file);
    }

    private boolean networkAvailable() {
        return NetworkAvailability.checkNetwork(getApplicationContext());
    }

    private void toast(boolean toastIs){
        if (toastIs)Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
        else Toast.makeText(JWMainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
    }

}
