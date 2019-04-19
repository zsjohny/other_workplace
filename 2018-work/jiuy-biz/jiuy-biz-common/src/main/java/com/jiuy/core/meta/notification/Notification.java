package com.jiuy.core.meta.notification;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class Notification extends BaseMeta<Long> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2588546676368607147L;
	//跳转类型
	//0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
	public static String TYPE_NO_CHOOSE  = "0" ;//商品
	public static String TYPE_PRODUCT  = "3" ;//商品
	
	private long id;
	private String title ; 
	private String abstracts ; 
	private int pushStatus ; 
	private int pageView ; 
	private long pushTime; 
	private String image; 
	private String type; 
	private String linkUrl; 
	private int status; 
	/**
	 * storeBusiness表的ID的集合，格式:\",1,2,3\
	 */
	private String storeIdArrays;
	private String pushTimeString;
	@JsonIgnore
	private long createTime ; 
	@JsonIgnore
	private long updateTime;
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
	public String getPushTimeString() {
        return pushTimeString;
	}
	public void setPushTimeString(String pushTimeString) {
		this.pushTimeString = pushTimeString;
	}

    public String getShowPushTime() {
        Date d = new Date(getPushTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
	@Override
	public String toString() {
		return "Notification [id=" + id + ", title=" + title + ", abstracts=" + abstracts + ", pushStatus=" + pushStatus
				+ ", pageView=" + pageView + ", pushTime=" + pushTime + ", image=" + image + ", type=" + type
				+ ", linkUrl=" + linkUrl + ", status=" + status + ", pushTimeString=" + pushTimeString + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
	@Override
	public Long getCacheId() {
		return null;
	}
	public String getStoreIdArrays() {
		return storeIdArrays;
	}
	public void setStoreIdArrays(String storeIdArrays) {
		this.storeIdArrays = storeIdArrays;
	} 
	
//	CREATE TABLE `yjj_Notification` (
//			  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
//			  `Title` text COMMENT '个推标题',
//			  `Abstracts` text COMMENT '摘要',
//			  `PushStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '推送状态 0待推送 1已推送',
//			  `PageView` int(11) NOT NULL DEFAULT '0' COMMENT '浏览量',
//			  `PushTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '定时推送的时间',
//			  `Image` varchar(256) NOT NULL COMMENT '推广图片',
//			  `Type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '跳转类型\n0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后 ',
//			  `LinkUrl` varchar(256) DEFAULT NULL COMMENT '跳转内容(跳转地址)\n0 ：未选择， 1：URL-网址， 2： 文章-文章id，3：商品-商品id，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流OrderNo 10：售后服务编号Id',
//			  `Status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态:0正常,-1删除',
//			  `CreateTime` bigint(20) NOT NULL,
//			  `UpdateTime` bigint(20) NOT NULL,
//			  PRIMARY KEY (`Id`)
//			) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8 COMMENT='个推表(APP系统通知)';
	
}
