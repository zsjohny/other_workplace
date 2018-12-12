package com.finace.miscroservice.activity.po;

/**
 * 红包类型实体类
 */
public class NewhbTypePO {

    private int hbleixingid;
    private String hbname;
    private int hbtype;
    private String hbdetail;
    private Double hbmoney;
    private Integer yday;

    public int getHbleixingid() {
        return hbleixingid;
    }

    public void setHbleixingid(int hbleixingid) {
        this.hbleixingid = hbleixingid;
    }

    public String getHbname() {
        return hbname;
    }

    public void setHbname(String hbname) {
        this.hbname = hbname;
    }

    public int getHbtype() {
        return hbtype;
    }

    public void setHbtype(int hbtype) {
        this.hbtype = hbtype;
    }

    public String getHbdetail() {
        return hbdetail;
    }

    public void setHbdetail(String hbdetail) {
        this.hbdetail = hbdetail;
    }

    public Double getHbmoney() {
        return hbmoney;
    }

    public void setHbmoney(Double hbmoney) {
        this.hbmoney = hbmoney;
    }

    public Integer getYday() {
        return yday;
    }

    public void setYday(Integer yday) {
        this.yday = yday;
    }
}
