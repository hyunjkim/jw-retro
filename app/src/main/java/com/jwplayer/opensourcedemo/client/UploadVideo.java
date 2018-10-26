package com.jwplayer.opensourcedemo.client;

import android.content.Context;
import android.util.Log;

import com.jwplayer.opensourcedemo.JWMainActivity;
import com.jwplayer.opensourcedemo.ToastUtil;
import com.jwplayer.opensourcedemo.data.JWPojo;
import com.jwplayer.opensourcedemo.network.NetworkAvailability;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadVideo {
    /**
     *@Header("Content-Type") String contentType,
     @Header("Authorization") String authorization,
     @Part("file") RequestBody file,
     @Path("path") String path,
     @Query("api_format") String format,
     @Query("key") String video_key,
     @Query("token") String token
      * */

    private static final String TAG = "JWPLAYER-LOGGER";

    public static void uploadVideo(final Context context, Retrofit retrofit, JWService jwService, Call<JWPojo> callJWAPIService, String apiFormat, String authentication, String intentpath, JWPojo upload){
        String key = upload.getMedia().getKey();
        String token = upload.getLink().getQuery().getToken();

        MultipartBody.Part multiBodyPart = getmultibodypart(intentpath);

        retrofit = JWRetrofitInstance.getJWRetrofitInstance("upload");

        jwService = retrofit.create(JWService.class);
        callJWAPIService = jwService.uploadVideoToJW(
                "multipart/form-data",
                authentication,
                apiFormat,
                key,
                token,
                multiBodyPart);

        callJWAPIService.enqueue(new Callback<JWPojo>() {
            @Override
            public void onResponse(Call<JWPojo> call, Response<JWPojo> response) {
                ToastUtil.toast(context,response.isSuccessful(),null);
                Log.i(TAG, "UPLOAD URL: " + call.request().url());
                Log.i(TAG, "UPLOAD State: " + response.isSuccessful());
                Log.i(TAG, "UPLOAD Message: " + response.message());
                Log.i(TAG, "UPLOAD Raw: " + response.raw());
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {
                Log.i(TAG, "FAILED TO UPLOAD: " + t.getMessage(), t);
                ToastUtil.toast(context,false,"FAILED TO UPLOAD ");
            }
        });

    }

    private static MultipartBody.Part getmultibodypart(String intentpath){
        File videoFile = new File(intentpath);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        return MultipartBody.Part.createFormData("file", "file", videoBody);
    }
}
