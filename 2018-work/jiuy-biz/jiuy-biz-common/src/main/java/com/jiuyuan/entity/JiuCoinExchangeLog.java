package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年12月16日 下午5:02:15
 * 
 */

public class JiuCoinExchangeLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1599310835042950581L;

	private long id;
	
	private long userId;
	
	private int type;
	
	private long relatedId;
	
	private int count;
	
	private int jiuCoin;
	
	private String content;
	
	private long createTime;

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
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}
	
}
