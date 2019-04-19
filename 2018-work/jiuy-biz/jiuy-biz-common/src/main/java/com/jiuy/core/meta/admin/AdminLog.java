/**
 * 
 */
package com.jiuy.core.meta.admin;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Never
 *
 */
public class AdminLog implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8839658191781144696L;


    public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOperateModel() {
		return operateModel;
	}
	public void setOperateModel(String operateModel) {
		this.operateModel = operateModel;
	}
	public String getOperateContent() {
		return operateContent;
	}
	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}
	public long getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.getCreateTime()));
	}
	
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private long userId;
    private String userName; // 用户名
    private String operateModel; // 用户操作模块
    private String operateContent; // 用户操作内容    
    private String ip; // 用户操作内容    
    private long createTime;


	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
    
    
}
