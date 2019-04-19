package com.jiuy.core.meta.coupon;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class Coupon implements Serializable{

    public Coupon() {
    }
	
    public Coupon(Long id, String code, String templateName, Integer type, Integer rangeType,
                  Long validityStartTime, Long validityEndTime, Integer isLimit, Long yJJNumber, Integer status) {
        super();
        this.id = id;
        this.code = code;
        this.templateName = templateName;
        this.type = type;
        this.rangeType = rangeType;
        this.validityStartTime = validityStartTime;
        this.validityEndTime = validityEndTime;
        this.isLimit = isLimit;
        this.yJJNumber = yJJNumber;
        this.status = status;
    }

    /**
     * 
     */
	private static final long serialVersionUID = 272646017895697773L;

	private Long id;
	
    private String code;
	
    private Long couponTemplateId;
	
    private String templateName;
	
	private Integer type;
	
	private Double money;
	
	private Integer rangeType;
	
	private String rangeContent;
	
	private Long validityStartTime;
	
	private Long validityEndTime;
	
	private Integer isLimit;
	
	private Integer coexist;
	
    private Long userId;
	
    private Long yJJNumber;
	
    private Integer status;
	
    private Integer pushStatus;
	
	private Long createTime;
	
	private Long updateTime;
	
	private String pushTitle;
	
	private String pushDescription;
	
	private String pushUrl;
	
	private String pushImage;
	
	private Long publishAdminId;
	
	private Long grantAdminId;
	
	private Integer getWay;
	
	private Long orderNo;
	
	private Double limitMoney;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getRangeType() {
		return rangeType;
	}

	public void setRangeType(Integer rangeType) {
		this.rangeType = rangeType;
	}

	public String getRangeContent() {
		return rangeContent;
	}

	public void setRangeContent(String rangeContent) {
		this.rangeContent = rangeContent;
	}

	public Long getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(Long validityStartTime) {
		this.validityStartTime = validityStartTime;
	}

	public Long getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(Long validityEndTime) {
		this.validityEndTime = validityEndTime;
	}

	public Integer getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Integer isLimit) {
		this.isLimit = isLimit;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCouponTemplateId() {
        return couponTemplateId;
    }

    public void setCouponTemplateId(Long couponTemplateId) {
        this.couponTemplateId = couponTemplateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getyJJNumber() {
        return yJJNumber;
    }

    public void setyJJNumber(Long yJJNumber) {
        this.yJJNumber = yJJNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }
    
	public String getPushTitle() {
		return pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	public String getPushDescription() {
		return pushDescription;
	}

	public void setPushDescription(String pushDescription) {
		this.pushDescription = pushDescription;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}
	
	public String getPushImage() {
		return pushImage;
	}

	public void setPushImage(String pushImage) {
		this.pushImage = pushImage;
	}

	public Object getRangeDetail() {
		if(rangeContent != null) {
			return JSON.parse(rangeContent);
		}
		return null;
	}

	public Long getPublishAdminId() {
		return publishAdminId;
	}

	public void setPublishAdminId(Long publishAdminId) {
		this.publishAdminId = publishAdminId;
	}

	public Long getGrantAdminId() {
		return grantAdminId;
	}

	public void setGrantAdminId(Long grantAdminId) {
		this.grantAdminId = grantAdminId;
	}
	
	public Integer getCoexist() {
		return coexist;
	}

	public void setCoexist(Integer coexist) {
		this.coexist = coexist;
	}

	public Integer getGetWay() {
		return getWay;
	}

	public void setGetWay(Integer getWay) {
		this.getWay = getWay;
	}
	
	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getUseStatus() {
		if (status == -1) {
			return "作废";
		} else if (status == 1) {
			return "已用";
		} else if (status ==0) {
			if (validityEndTime != 0 && validityEndTime < System.currentTimeMillis()) {
				return "过期";
			}
		}
		
		return "未使用";
	}
	
	
}
