package com.jiuyuan.entity;

import java.io.Serializable;
/**
 * 用户分享记录
 * @author zhuzl
 *
 */
public class UserSharedRecord implements Serializable {
	
	private static final long serialVersionUID = 2712120561323453025L;

	private long id;
	
	private long userId;
	/**
	 * 分享内容类型 0：其他 1：商品 2:文章
	 */
	private int type;
	/**
	 * 相关id
	 */
	private long relatedId;
	/**
	 * 分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博
	 */
	private int channel;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
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

	@Override
	public String toString() {
		return "UserSharedRecord [id=" + id + ", userId=" + userId + ", type=" + type + ", relatedId=" + relatedId
				+ ", channel=" + channel + ", status=" + status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
	/*
	CREATE TABLE `yjj_UserSharedRecord` (
			  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
			  `UserId` bigint(20) NOT NULL COMMENT '用户Id',
			  `Type` tinyint(4) DEFAULT '0' COMMENT '分享内容类型 0：其他 1：商品 2:文章',
			  `RelatedId` bigint(20) DEFAULT '0' COMMENT '相关id',
			  `Channel` tinyint(4) DEFAULT '0' COMMENT '分享渠道 0：其他 1：微信好友 2:微信朋友圈 3：QQ 4:QQ空间 5:腾讯微博 6:新浪微博',
			  `Status` tinyint(4) DEFAULT '0' COMMENT '状态：0正常，-1删除',
			  `CreateTime` bigint(20) NOT NULL COMMENT '记录创建时间',
			  `UpdateTime` bigint(20) NOT NULL COMMENT '记录更新时间',
			  PRIMARY KEY (`Id`)
			) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户分享记录表';
	*/
}
