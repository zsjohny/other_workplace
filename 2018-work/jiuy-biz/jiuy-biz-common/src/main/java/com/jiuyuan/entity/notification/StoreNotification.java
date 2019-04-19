package com.jiuyuan.entity.notification;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @author LWS
 *
 */
public class StoreNotification implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -685358288881500663L;
	public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAbstracts() {
        return abstracts;
    }
    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }
    public int getPushStatus() {
        return pushStatus;
    }
    public void setPushStatus(int pushStatus) {
        this.pushStatus = pushStatus;
    }
    public int getPageView() {
        return pageView;
    }
    public void setPageView(int pageView) {
        this.pageView = pageView;
    }
    public long getPushTime() {
        return pushTime;
    }
    public String getPushTimeStr() {
    	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return sdf.format(pushTime);
    }
    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public int getUserPageView() {
		return userPageView;
	}
	public void setUserPageView(int userPageView) {
		this.userPageView = userPageView;
	}
	private long id;
    private String title;
    private String abstracts;
    private int pushStatus;
    private int pageView;
    private long pushTime;
    private String image;
    private String type;
    private String linkUrl;
    private int status;
    private long createTime;
    private long updateTime;
    private int userPageView;
}