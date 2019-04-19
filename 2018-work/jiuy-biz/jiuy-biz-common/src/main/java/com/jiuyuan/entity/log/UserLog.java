/**
 * 
 */
package com.jiuyuan.entity.log;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2017年2月17日 下午3:39:17
*/
public class UserLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2269936005464075331L;

	private long id;
	private long userId;
	private String cid;
	private long createTime;
	private long updateTime;
	private String ip;
	private int platform;
	private String version;
	private int net;
	
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
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getNet() {
		return net;
	}
	public void setNet(int net) {
		this.net = net;
	}	
	
}
