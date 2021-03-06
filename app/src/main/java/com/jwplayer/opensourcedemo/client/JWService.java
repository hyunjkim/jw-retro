package com.jwplayer.opensourcedemo.client;

import com.jwplayer.opensourcedemo.data.JWPojo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface JWService {

    @GET("/v1/videos/create")
    Call<JWPojo> createVideoToJW(@Query("api_format") String api_format,
                                 @Query("api_key") String api_key,
                                 @Query("api_nonce") String api_nonce,
                                 @Query("api_timestamp") String api_timestamp,
                                 @Query("api_signature") String api_signature,
                                 @Query("api_key") String api_key1);

    @Multipart
    @POST("/v1/videos/upload")
    Call<JWPojo> uploadVideoToJW(@Header("enctype") String contenttype,
                                 @Header("Authorization") String authorization,
                                 @Query("api_format") String format,
                                 @Query("key") String video_key,
                                 @Query("token") String token,
                                 @Part MultipartBody.Part video);

}