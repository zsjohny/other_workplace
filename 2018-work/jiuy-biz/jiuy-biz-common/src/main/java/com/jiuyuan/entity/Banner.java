package com.jiuyuan.entity;

import java.io.Serializable;

public class Banner implements Serializable {

    private static final long serialVersionUID = 3635044975353764919L;

    private String imageUrl;

    private String targetUrl;

    public Banner(String imageUrl, String targetUrl) {
        this.imageUrl = imageUrl;
        this.targetUrl = targetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
