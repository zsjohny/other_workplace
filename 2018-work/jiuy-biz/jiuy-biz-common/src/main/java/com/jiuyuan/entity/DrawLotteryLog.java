package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年11月2日 下午4:57:09
 * 
 */

public class DrawLotteryLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3111988568840644438L;

	private long id;
	
	private int type;
	
	private String name;
	
	private int count;
	
	private String rankName;
	
	private long userId;
	
	private long relatedId;
	
	private int status;
	
	private int jiuCoin;
	
	private long createTime;
	
	public DrawLotteryLog() {
		super();
	}

	public DrawLotteryLog(int type, String name, String rankName, long userId, long relatedId, long createTime, int status, int jiuCoin, int count) {
		super();
		this.name = name;
		this.rankName = rankName;
		this.type = type;
		this.userId = userId;
		this.relatedId = relatedId;
		this.createTime = createTime;
		this.status = status;
		this.jiuCoin = jiuCoin;
		this.count = count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
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

	public int getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(int jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
