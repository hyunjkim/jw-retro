package com.jwplayer.opensourcedemo.client;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class JWAuthentication {

//    TODO: ADD YOUR KEY AND SECRET
    private final String API_KEY = "";
    private final String API_SECRET = "";
    private String api_format = "json",
            api_generation,
            api_signature,
            authentication,
            api_nonce,
            api_timestamp;

    /*
    * For more info: https://developer.android.com/reference/java/security/MessageDigest
    * */
    public void createSignature(String params){

        generateApiSign(params);


        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = api_signature.getBytes("UTF-8");
            md.update(bytes,0,bytes.length);
            api_signature = encodeHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // An authenticated API call will look like this:
        authentication = api_generation + "&api_signature=" + api_signature + "&api_key=" + API_KEY;
    }

    private void generateApiSign(String params) {

        api_nonce= String.valueOf(System.currentTimeMillis());
        api_timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        api_generation = "api_format=" + api_format+
                         "&api_key=" + API_KEY +
                         "&api_nonce=" + api_nonce +
                         "&api_timestamp=" + api_timestamp;

        if(params.length()>0) api_generation = api_generation.concat(params);

        // The secret is added and SHA-1 digest is calculated. Secret is added to the end of the SBS:
        api_signature = api_generation + API_SECRET;
    }

    /**
     * Copied from :https://code.i-harness.com/en/q/5b41f2
     * */
    private static String encodeHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString((int) aByte & 0xff, 16));
        }

        return hex.toString();
    }

    public String getApiGeneration() {
        return api_generation;
    }

    public String getApiSignature() {
        return api_signature;
    }

    public String getApiFormat() {
        return api_format;
    }

    public String getApikey() {
        return API_KEY;
    }

    public String getAuthentication(){
        return authentication;
    }

    public String getApiNonce() {
        return api_nonce;
    }

    public String getApiTimestamp() {
        return api_timestamp;
    }
}
