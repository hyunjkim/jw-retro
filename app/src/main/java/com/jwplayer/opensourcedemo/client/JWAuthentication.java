package com.jwplayer.opensourcedemo.client;

import com.jwplayer.opensourcedemo.util.JWLoggerUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JWAuthentication {

    //    TODO: ADD YOUR KEY AND SECRET
    private static JWAuthentication instance;
    private final String API_KEY = "";
    private final String API_SECRET = "";
    private String api_format = "json",
            api_generation,
            api_signature,
            authentication,
            api_nonce,
            api_timestamp;

    public static JWAuthentication getInstance() {
        if (instance == null) {
            instance = new JWAuthentication();
        }
        return instance;
    }

    /*
     * For more info: https://developer.android.com/reference/java/security/MessageDigest
     * */
    void createSignature(String params) {

        generateApiSign(params);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = api_signature.getBytes("UTF-8");
            md.update(bytes, 0, bytes.length);
            api_signature = encodeHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // An authenticated API call will look like this:
        authentication = api_generation + "&api_signature=" + api_signature + "&api_key=" + API_KEY;
    }

    // TODO : Might be used for S3 uploads
    static String encodeSign(String sign) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = sign.getBytes("UTF-8");
            md.update(bytes, 0, bytes.length);
            return encodeHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void generateApiSign(String params) {

        api_nonce = String.valueOf(System.currentTimeMillis());
        api_timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        api_generation = "api_format=" + api_format +
                "&api_key=" + API_KEY +
                "&api_nonce=" + api_nonce +
                "&api_timestamp=" + api_timestamp;

        if (params.length() > 0) {
            api_generation += params;
            JWLoggerUtil.log(api_generation);
        }

        // The secret is added and SHA-1 digest is calculated. Secret is added to the end of the SBS:
        api_signature = api_generation + API_SECRET;
    }

    /**
     * Copied from :https://code.i-harness.com/en/q/5b41f2
     */
    static String encodeHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString((int) aByte & 0xff, 16));
        }
        return hex.toString();
    }

    String getApiGeneration() {
        return api_generation;
    }

    String getApiSignature() {
        return api_signature;
    }

    String getApiFormat() {
        return api_format;
    }

    String getApikey() {
        return API_KEY;
    }

    String getAuthentication() {
        return authentication;
    }

    String getApiNonce() {
        return api_nonce;
    }

    String getApiTimestamp() {
        return api_timestamp;
    }
}
