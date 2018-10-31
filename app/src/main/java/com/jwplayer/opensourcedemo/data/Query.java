package com.jwplayer.opensourcedemo.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("AWSAccessKeyId")
    @Expose
    private String aWSAccessKeyId;
    @SerializedName("Expires")
    @Expose
    private String expires;
    @SerializedName("Signature")
    @Expose
    private String signature;

    public String getAWSAccessKeyId() {
        return aWSAccessKeyId;
    }

    public void setAWSAccessKeyId(String aWSAccessKeyId) {
        this.aWSAccessKeyId = aWSAccessKeyId;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
