package com.jiuyuan.entity.account;

import java.io.Serializable;

public class UserBankCardSign implements Serializable {

    private static final long serialVersionUID = -2960266889540167523L;

    private long id;

    private long userId;

    private long createTime;

    private long updateTime;
    
    private String pno;
    
    private String seq;
    
    private String mobile;
    
    private String rskLvl;
    
    private int isSigned;

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

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRskLvl() {
		return rskLvl;
	}

	public void setRskLvl(String rskLvl) {
		this.rskLvl = rskLvl;
	}

	public int getIsSigned() {
		return isSigned;
	}

	public void setIsSigned(int isSigned) {
		this.isSigned = isSigned;
	}



}
