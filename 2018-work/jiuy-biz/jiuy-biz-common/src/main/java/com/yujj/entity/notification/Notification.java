package com.yujj.entity.notification;

import java.io.Serializable;

/**
 * 个推表(APP系统通知)
 *
 */
public class Notification implements Serializable{
    
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
    private String title;//个推标题
    private String abstracts;//摘要
    private int pushStatus;//推送状态 0待推送 1已推送
    private int pageView;//浏览量
    private long pushTime;//定时推送的时间
    private String image;//推广图片
    private String type;//跳转类型\n0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后
    private String linkUrl;//跳转内容(跳转地址)\n0 ：未选择， 1：URL-网址， 2： 文章-文章id，3：商品-商品id，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后
    private int status;//状态:0正常,-1删除
    private long createTime;
    private long updateTime;
    
    private int userPageView;
    
}