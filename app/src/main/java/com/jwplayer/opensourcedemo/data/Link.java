package com.jwplayer.opensourcedemo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Link {

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("query")
    @Expose
    private Query query;
    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("address")
    @Expose
    private String address;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}