package com.jiuyuan.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.ad.AdEnum;


public class AdConfig implements Serializable {

    private static final long serialVersionUID = 3939640770412524726L;

    @JsonIgnore
    private long id;

    @JsonIgnore
    private AdEnum adType;

    @JsonIgnore
    private int adOrder;

    private boolean newPage;

    private String imageUrl;

    private String linkUrl;

    @JsonIgnore
    private long updateTime;

    @JsonIgnore
    private long createTime;

    private String adtitle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AdEnum getAdType() {
        return adType;
    }

    public void setAdType(AdEnum adType) {
        this.adType = adType;
    }

    public int getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(int adOrder) {
        this.adOrder = adOrder;
    }

    public boolean isNewPage() {
        return newPage;
    }

    public void setNewPage(boolean newPage) {
        this.newPage = newPage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAdtitle() {
        return adtitle;
    }

    public void setAdtitle(String adtitle) {
        this.adtitle = adtitle;
    }

}
