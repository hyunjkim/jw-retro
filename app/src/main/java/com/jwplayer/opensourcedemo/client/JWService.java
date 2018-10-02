package com.jwplayer.opensourcedemo.client;

import com.jwplayer.opensourcedemo.data.JWPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    @POST("{path}")
    Call<JWPojo> uploadVideoToJW(@Header("Content-Type") String contentType,
                                 @Header("Authorization") String authorization,
                                 @Part("file") RequestBody file,
                                 @Path("path") String path,
                                 @Query("api_format") String format,
                                 @Query("key") String video_key,
                                 @Query("token") String token);

}