package com.jiuy.core.meta.memberstatistics;
/**
* @author WuWanjian
* @version 创建时间: 2017年3月7日 上午10:25:03
*/
public class TemplateStatisticsBean {
	private long srcId;
	
	private String name;
	
	private int pv;
	
	private int uv;
	
	private int userCount;
	
	private String ip;
	
	private int relatedOrderCount;
	
	public long getSrcId() {
		return srcId;
	}

	public void setSrcId(long srcId) {
		this.srcId = srcId;
	}

	public String getName() {
		if(srcId==0){
			return "首页模板";
		}else if(srcId==-29){
			return "品类模板";
		}else if(srcId==-2){
			return "玖币商城";
		}else if(srcId==-4){
			return "看了又看";
		}else {
			return name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getConversion() {
		return pv==0?0: ((float)relatedOrderCount * 100)/pv;
	}

	public int getRelatedOrderCount() {
		return relatedOrderCount;
	}

	public void setRelatedOrderCount(int relatedOrderCount) {
		this.relatedOrderCount = relatedOrderCount;
	}
	
}
