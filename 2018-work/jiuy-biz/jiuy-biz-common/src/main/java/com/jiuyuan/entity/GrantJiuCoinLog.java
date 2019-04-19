package com.jiuyuan.entity;
/**
* @author WuWanjian
* @version 创建时间: 2016年11月25日 上午9:59:41
*/
public class GrantJiuCoinLog {
	
	private long id;
	
	private int type;
	
	private String content;
	
	private int grantUnitJiuCoin;
	
	private int grantUserCount;
	
	private int grantTotalJiuCoin;
	
	private long createTime;
	
	public GrantJiuCoinLog(){
		super();
	}
	
	public GrantJiuCoinLog(int type, String content, int grantUnitJiuCoin, int grantUserCount, int grantTotalJiuCoin,
			long createTime) {
		super();
		this.type = type;
		this.content = content;
		this.grantUnitJiuCoin = grantUnitJiuCoin;
		this.grantUserCount = grantUserCount;
		this.grantTotalJiuCoin = grantTotalJiuCoin;
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGrantUnitJiuCoin() {
		return grantUnitJiuCoin;
	}

	public void setGrantUnitJiuCoin(int grantUnitJiuCoin) {
		this.grantUnitJiuCoin = grantUnitJiuCoin;
	}

	public int getGrantUserCount() {
		return grantUserCount;
	}

	public void setGrantUserCount(int grantUserCount) {
		this.grantUserCount = grantUserCount;
	}

	public int getGrantTotalJiuCoin() {
		return grantTotalJiuCoin;
	}

	public void setGrantTotalJiuCoin(int grantTotalJiuCoin) {
		this.grantTotalJiuCoin = grantTotalJiuCoin;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
