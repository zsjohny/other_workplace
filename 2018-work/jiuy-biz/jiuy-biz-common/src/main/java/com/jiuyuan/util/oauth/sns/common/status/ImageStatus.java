package com.jiuyuan.util.oauth.sns.common.status;

public class ImageStatus extends Status implements IImageStatus {

    private String imageUrl;

    public ImageStatus(String title, String status, String link, String imageUrl) {
        super(title, status, link);
        this.imageUrl = imageUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}
