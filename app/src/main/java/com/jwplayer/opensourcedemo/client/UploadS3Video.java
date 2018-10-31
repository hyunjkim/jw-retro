package com.jwplayer.opensourcedemo.client;

import android.os.Build;
import android.util.Log;

import com.jwplayer.opensourcedemo.JWMainActivity;
import com.jwplayer.opensourcedemo.data.JWPojo;
import com.jwplayer.opensourcedemo.util.JWLoggerUtil;
import com.jwplayer.opensourcedemo.util.ToastUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class UploadS3Video {
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

    static void uploadVideo(final JWMainActivity jwMainActivity,String authentication, String intentpath, JWPojo upload){
        String videokey = upload.getMedia().getKey();
        String awskey = upload.getLink().getQuery().getAWSAccessKeyId();
        String expires = upload.getLink().getQuery().getExpires();
        String signature = upload.getLink().getQuery().getSignature();

        String address = upload.getLink().getAddress();

        JWLoggerUtil.log("signature: " + signature);

        MultipartBody.Part multiBodyPart = getmultibodypart(intentpath);

        Retrofit retrofit = JWRetrofitInstance.getJWRetrofitInstance(address);

        JWService jwService = retrofit.create(JWService.class);
        Call<JWPojo> callJWAPIService = jwService.uploadS3VideoToJW(
                "multipart/form-data",
                authentication,
                videokey,
                awskey,
                expires,
                signature,
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

                Log.i(TAG, "FAILED TO UPLOAD: " + call.request().body());
                Log.i(TAG, "FAILED TO UPLOAD: " + call.request().url());
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
