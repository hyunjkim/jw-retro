package com.jwplayer.opensourcedemo.client;

import android.util.Log;

import com.jwplayer.opensourcedemo.JWLoggerUtil;
import com.jwplayer.opensourcedemo.JWMainActivity;
import com.jwplayer.opensourcedemo.ToastUtil;
import com.jwplayer.opensourcedemo.data.JWPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class CreateVideo{


    public static void createVideo(final JWMainActivity jwMainActivity, final JWAuthentication authentication, final String intentpath) {

        authentication.createSignature("");

        final String apiFormat = authentication.getApiFormat();
        String key = authentication.getApikey();
        String nonce = authentication.getApiNonce();
        String timestamp = authentication.getApiTimestamp();
        String token = authentication.getApiSignature();

        Retrofit retrofit = JWRetrofitInstance.getJWRetrofitInstance("create");
        JWService jwService = retrofit.create(JWService.class);
        Call<JWPojo> callJWAPIService = jwService.createVideoToJW(apiFormat, key, nonce, timestamp, token, key);

        jwMainActivity.showProgress();

        callJWAPIService.enqueue(new Callback<JWPojo>() {

            @Override
            public void onResponse(Call<JWPojo> call, Response<JWPojo> response) {

                jwMainActivity.endProgress();

                JWPojo mPojo = response.body();
                String errorMsg = "No information was received (HINT: Did you add your Secret and key?)";
                String message = response.message();
                JWLoggerUtil.log("SUCCESS MESSAGE:" + message);
                jwMainActivity.endProgress();

                if (response.isSuccessful() && mPojo != null) {
                    UploadVideo.uploadVideo(jwMainActivity, apiFormat, authentication.getAuthentication(), intentpath, mPojo);
                    ToastUtil.toast(jwMainActivity,true,null);
                    jwMainActivity.updateStatus(message);
                } else {
                    ToastUtil.toast(jwMainActivity,false,errorMsg);
                    jwMainActivity.updateStatus(message + errorMsg);
                }
            }

            @Override
            public void onFailure(Call<JWPojo> call, Throwable t) {

                jwMainActivity.endProgress();

                Log.i(TAG, "FAILED to create a video! 1) " + t.getMessage(), t);
                Log.i(TAG, "FAILED to create a video! 1a) " +  t.getLocalizedMessage(), t);
                Log.i(TAG, "FAILED to create a video! 2) " + call.request().body().toString(), t);
                Log.i(TAG, "FAILED to create a video! 3) " + call.request().toString(), t);
                ToastUtil.toast(jwMainActivity,false,"FAILED to create a video!");

                jwMainActivity.updateStatus("Failed to create a video");
                jwMainActivity.updateStatus(call.request().body().toString());
                jwMainActivity.updateStatus(call.request().toString());
            }
        });

    }
}
