package com.finace.miscroservice.commons.entity;

public class UserRedPackets {

    private int hbid;
    private int userid;
    private int hbleixingid;
    private String hbstartime;
    private String hbendtime;
    private int hbstatus;
    private Double hbmoney;
    private String hbdetail;
    private int hbtype;
    private String yxq;//临时借用
    private String hbname; //红包名称
    private Integer smoney;
    private Integer sday;
    private int inviter; //邀请人id
    private int flag;	//红包到期发送mq消息状态

    public int getHbid() {
        return hbid;
    }

    public void setHbid(int hbid) {
        this.hbid = hbid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getHbleixingid() {
        return hbleixingid;
    }

    public void setHbleixingid(int hbleixingid) {
        this.hbleixingid = hbleixingid;
    }

    public String getHbstartime() {
        return hbstartime;
    }

    public void setHbstartime(String hbstartime) {
        this.hbstartime = hbstartime;
    }

    public String getHbendtime() {
        return hbendtime;
    }

    public void setHbendtime(String hbendtime) {
        this.hbendtime = hbendtime;
    }

    public int getHbstatus() {
        return hbstatus;
    }

    public void setHbstatus(int hbstatus) {
        this.hbstatus = hbstatus;
    }

    public Double getHbmoney() {
        return hbmoney;
    }

    public void setHbmoney(Double hbmoney) {
        this.hbmoney = hbmoney;
    }

    public String getHbdetail() {
        return hbdetail;
    }

    public void setHbdetail(String hbdetail) {
        this.hbdetail = hbdetail;
    }

    public int getHbtype() {
        return hbtype;
    }

    public void setHbtype(int hbtype) {
        this.hbtype = hbtype;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getHbname() {
        return hbname;
    }

    public void setHbname(String hbname) {
        this.hbname = hbname;
    }

    public Integer getSmoney() {
        return smoney;
    }

    public void setSmoney(Integer smoney) {
        this.smoney = smoney;
    }

    public Integer getSday() {
        return sday;
    }

    public void setSday(Integer sday) {
        this.sday = sday;
    }

    public int getInviter() {
        return inviter;
    }

    public void setInviter(int inviter) {
        this.inviter = inviter;
    }

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

    @Override
    public String toString() {
        return "UserRedPackets{" +
                "hbid=" + hbid +
                ", userid=" + userid +
                ", hbleixingid=" + hbleixingid +
                ", hbstartime='" + hbstartime + '\'' +
                ", hbendtime='" + hbendtime + '\'' +
                ", hbstatus=" + hbstatus +
                ", hbmoney=" + hbmoney +
                ", hbdetail='" + hbdetail + '\'' +
                ", hbtype=" + hbtype +
                ", yxq='" + yxq + '\'' +
                ", hbname='" + hbname + '\'' +
                ", smoney=" + smoney +
                ", sday=" + sday +
                ", inviter=" + inviter +
                ", flag=" + flag +
                '}';
    }
}
