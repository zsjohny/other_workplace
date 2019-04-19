package com.jiuy.core.meta.comment;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class Comment extends BaseMeta<Long> implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1456396742722733920L;

	private long id;
	
	private long orderId;
	
	private long userId;
	
	private long productId;
	
	private long skuId;
	
	private long brandId;
	
	private int liker;
	
	private String content;
	
	private String imageUrl;
	
	private int anonymity;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	private String propertyIds;
	
    private String userNickname;
    
    private String userIcon;
	
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
	
	public long getProductId() {
		return productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public long getSkuId() {
		return skuId;
	}
	
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}	
	
	public long getBrandId() {
		return brandId;
	}
	
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}	
	
	public int getLiker() {
		return liker;
	}
	
	public void setLiker(int liker) {
		this.liker = liker;
	}
	
	public String getContent() {
		if(StringUtils.equals(null, content)) {
			return "";
		}
		// 显示屏蔽
    	String patternString = "草|操";
    	Pattern pattern = Pattern.compile(patternString);
    	Matcher matcher = pattern.matcher(content);
    	
		return matcher.replaceAll("*");
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@JsonIgnore
	public String getEscapeContent() {
		return StringEscapeUtils.escapeHtml4(content);
	}
	
	@JsonIgnore
	public void setEscapeContent(String content) {
		this.content = StringEscapeUtils.unescapeHtml4(content);
	}
	
	public JSONArray getImageUrl() {
		if(StringUtils.equals(null, imageUrl)) {
			imageUrl = "[]";
		}
		
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = JSON.parseArray(imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public int getAnonymity() {
		return anonymity;
	}
	
	public void setAnonymity(int anonymity) {
		this.anonymity = anonymity;
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
	
	public String getUserNickname() {		
		return StringUtils.left(userNickname, 2) + "***" + StringUtils.right(userNickname, 4);
	}
	
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserIcon() {
		return userIcon;
	}
	
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getPropertyIds() {
		return propertyIds;
	}
	
	public void setPropertyIds(String propertyIds) {
		this.propertyIds = propertyIds;
	}

	public long getOrderId() {
		return orderId;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	@Override
	public Long getCacheId() {
		return null;
	}	
}
