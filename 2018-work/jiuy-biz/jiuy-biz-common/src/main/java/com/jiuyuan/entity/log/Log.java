/**
 * 
 */
package com.jiuyuan.entity.log;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2017年2月9日 上午11:49:53
*/
public class Log implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6837314702677473320L;
	
	private long logId;
	private long userId;
	private String ip;
	private String cid;
	private int platform;
	private String version;
	private int net;
	private int status;
	private long srcId;
	private long srcLogId;
	private String srcRelatedId;
	private long pageId;
	private long enterTime;
	private long loadFinishTime;
	private long exitTime;
	private long duration;
	private long createTime;
	
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getSrcId() {
		return srcId;
	}
	public void setSrcId(long srcId) {
		this.srcId = srcId;
	}
	public long getSrcLogId() {
		return srcLogId;
	}
	public void setSrcLogId(long srcLogId) {
		this.srcLogId = srcLogId;
	}
	public String getSrcRelatedId() {
		return srcRelatedId;
	}
	public void setSrcRelatedId(String srcRelatedId) {
		this.srcRelatedId = srcRelatedId;
	}
	public long getPageId() {
		return pageId;
	}
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	public long getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}
	public long getLoadFinishTime() {
		return loadFinishTime;
	}
	public void setLoadFinishTime(long loadFinishTime) {
		this.loadFinishTime = loadFinishTime;
	}
	public long getExitTime() {
		return exitTime;
	}
	public void setExitTime(long exitTime) {
		this.exitTime = exitTime;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}	

	@Override
	public String toString() {
		return "Log [logId=" + logId + ", userId=" + userId + ", ip=" + ip + ", cid=" + cid + ", platform=" + platform
				+ ", version=" + version + ", net=" + net + ", status=" + status + ", srcId=" + srcId + ", srcLogId="
				+ srcLogId + ", srcRelatedId=" + srcRelatedId + ", pageId=" + pageId + ", enterTime=" + enterTime
				+ ", loadFinishTime=" + loadFinishTime + ", exitTime=" + exitTime + ", duration=" + duration
				+ ", createTime=" + createTime + "]";
	}
	
}
