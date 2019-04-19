package com.jiuyuan.entity.account;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Address implements Serializable {

    private static final long serialVersionUID = 116542063818076543L;

    private long userId;//会员用
    
    private long storeId;//门店

    private long addrId;

    private String receiverName;

    private String provinceName;

    private String cityName;

    private String districtName;

    private String addrDetail;

    private String mailCode;

    private String telephone;

    private String fixPhone;

    private String addrFull;

    @JsonIgnore
    private int status;

    private boolean isDefault;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAddrId() {
        return addrId;
    }

    public void setAddrId(long addrId) {
        this.addrId = addrId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFixPhone() {
        return fixPhone;
    }

    public void setFixPhone(String fixPhone) {
        this.fixPhone = fixPhone;
    }

    public String getAddrFull() {
        if (StringUtils.isNotBlank(addrFull)) {
            return addrFull;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getProvinceName());
        if (!StringUtils.equals(getProvinceName(), getCityName())) {
            builder.append(getCityName());
        }
        builder.append(getDistrictName()).append(getAddrDetail());
        return builder.toString();
    }

    public void setAddrFull(String addrFull) {
        this.addrFull = addrFull;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
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

    @JsonIgnore
    public String getExpressInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append(getReceiverName());
        builder.append(", ");
        builder.append(StringUtils.defaultString(getTelephone(), getFixPhone()));
        builder.append(", ");
        builder.append(getAddrFull());
        if (StringUtils.isNotBlank(getMailCode())) {
            builder.append(", ");
            builder.append(getMailCode());
        }
        return builder.toString();
    }

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
    

	@Override
	public String toString() {
		return "Address [userId=" + userId + ", addrId=" + addrId + ", receiverName=" + receiverName + ", provinceName="
				+ provinceName + ", cityName=" + cityName + ", districtName=" + districtName + ", addrDetail="
				+ addrDetail + ", mailCode=" + mailCode + ", telephone=" + telephone + ", fixPhone=" + fixPhone
				+ ", addrFull=" + addrFull + ", status=" + status + ", isDefault=" + isDefault + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
}
