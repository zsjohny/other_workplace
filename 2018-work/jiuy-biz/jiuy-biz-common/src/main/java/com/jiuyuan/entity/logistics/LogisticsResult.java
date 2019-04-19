package com.jiuyuan.entity.logistics;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LogisticsResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String no;
    private boolean ischeck;
    private String com;
    private String company;
    private Date updatetime;
    private List<LogisticsData> data;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public boolean isIscheck() {
		return ischeck;
	}
	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public List<LogisticsData> getData() {
		return data;
	}
	public void setData(List<LogisticsData> data) {
		this.data = data;
	}
    
}
