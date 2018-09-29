package com.jwplayer.opensourcedemo.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JWPojo {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("link")
    @Expose
    private Link link;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }


}
