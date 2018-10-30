package com.jwplayer.opensourcedemo.client;

import android.content.Context;
import android.util.Log;

import com.jwplayer.opensourcedemo.JWMainActivity;
import com.jwplayer.opensourcedemo.ToastUtil;
import com.jwplayer.opensourcedemo.data.JWPojo;

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

    static void uploadVideo(final JWMainActivity jwMainActivity, String apiFormat, String authentication, String intentpath, JWPojo upload){
        String key = upload.getMedia().getKey();
        String token = upload.getLink().getQuery().getToken();

        MultipartBody.Part multiBodyPart = getmultibodypart(intentpath);

        Retrofit retrofit = JWRetrofitInstance.getJWRetrofitInstance("upload");

        JWService jwService = retrofit.create(JWService.class);
        Call<JWPojo> callJWAPIService = jwService.uploadVideoToJW(
                "multipart/form-data",
                authentication,
                apiFormat,
                key,
                token,
                multiBodyPart);

        jwMainActivity.showProgress();

        callJWAPIService.enqueue(new Callback<JWPojo>() {

            @Override
            public void onResponse(Call<JWPojo> call, Response<JWPojo> response) {

                jwMainActivity.endProgress();

                String message = response.raw().toString();
                ToastUtil.toast(jwMainActivity,response.isSuccessful(),null);

                Log.i(TAG, "UPLOAD State: " + response.isSuccessful());
                Log.i(TAG, "UPLOAD Message: " + message);

                jwMainActivity.updateStatus(message);
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {

                jwMainActivity.endProgress();

                Log.i(TAG, "FAILED TO UPLOAD: " + t.getMessage(), t);
                ToastUtil.toast(jwMainActivity,false,"FAILED TO UPLOAD ");
                jwMainActivity.updateStatus("FAILED TO UPLOAD");
            }
        });

    }

    private static MultipartBody.Part getmultibodypart(String intentpath){
        File videoFile = new File(intentpath);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        return MultipartBody.Part.createFormData("file", "file", videoBody);
    }
}
