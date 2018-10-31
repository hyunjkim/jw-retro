package com.jwplayer.opensourcedemo.client;

import com.jwplayer.opensourcedemo.data.JWPojo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JWService {

    @GET("/v1/videos/create/")
    Call<JWPojo> createVideoToJW(@Query("api_format") String api_format,
                                 @Query("api_key") String api_key,
                                 @Query("api_nonce") String api_nonce,
                                 @Query("api_timestamp") String api_timestamp,
                                 @Query("upload_method") String s3,
                                 @Query("upload_content_type") String mediatype,
                                 @Query("api_signature") String api_signature,
                                 @Query("api_key") String api_key1);

    @Multipart
    @POST("/v1/videos/upload")
    Call<JWPojo> uploadVideoToJW(@Header("enctype") String content_type,
                                 @Header("Authorization") String authorization,
                                 @Query("api_format") String format,
                                 @Query("key") String video_key,
                                 @Query("token") String token,
                                 @Part MultipartBody.Part video);

    @Multipart
    @POST("/{videokey}")
    Call<JWPojo> uploadS3VideoToJW(@Header("enctype") String content_type,
                                   @Header("Authorization") String authorization,
                                   @Path("videokey") String videokey,
                                   @Query("AWSAccessKeyId") String awskey,
                                   @Query("Expires") String expires,
                                   @Query("Signature") String signature,
                                   @Part MultipartBody.Part video);

}