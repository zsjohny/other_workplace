package com.yujj.entity.order;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;

public class Coupon implements Serializable{

	public Coupon() {
    }
	
    public Coupon(Long id, String code, String templateName, Integer type, Integer rangeType,
                  Long validityStartTime, Long validityEndTime, Integer isLimit, Long yJJNumber, Integer status, Double limitMoney) {
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
        this.limitMoney = limitMoney;
    }

    /**
     * 
     */
	private static final long serialVersionUID = 272646017895697773L;

	private long id;
	
    private String code;
	
    private long couponTemplateId;
	
    private String templateName;
	
	private int type;
	
	private double money;
	
	private int rangeType;
	
	private String rangeContent;
	
	private long validityStartTime;
	
	private long validityEndTime;
	
	private int isLimit;
	
	private int coexist;
	
    private long userId;
	
    private long yJJNumber;
	
    private int status;
	
    private int pushStatus;
	
	private long createTime;
	
	private long updateTime;
	
	private String pushTitle;
	
	private String pushDescription;
	
	private String pushUrl;
	
	private String pushImage;
	
	private long publishAdminId;
	
	private long grantAdminId;
	
	private long orderNo;
	
	private int getWay;
	
	private List<String> couponUseTips;
	
	private String relatedUrl;
	
	private double actualDiscount;
	
	private double limitMoney;


	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCouponTemplateId() {
		return couponTemplateId;
	}

	public void setCouponTemplateId(long couponTemplateId) {
		this.couponTemplateId = couponTemplateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getRangeType() {
		return rangeType;
	}

	public void setRangeType(int rangeType) {
		this.rangeType = rangeType;
	}

	public String getRangeContent() {
		return rangeContent;
	}

	public void setRangeContent(String rangeContent) {
		this.rangeContent = rangeContent;
	}

	public long getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(long validityStartTime) {
		this.validityStartTime = validityStartTime;
	}

	public long getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(long validityEndTime) {
		this.validityEndTime = validityEndTime;
	}

	public int getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(int isLimit) {
		this.isLimit = isLimit;
	}

	public int getCoexist() {
		return coexist;
	}

	public void setCoexist(int coexist) {
		this.coexist = coexist;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getyJJNumber() {
		return yJJNumber;
	}

	public void setyJJNumber(long yJJNumber) {
		this.yJJNumber = yJJNumber;
	}

	public int getStatus() {
		if(this.getValidityStartTime() > 0 && this.getValidityEndTime() > 0){
			if(System.currentTimeMillis() > this.getValidityEndTime() || System.currentTimeMillis() < this.getValidityStartTime()){
				return OrderCouponStatus.SCRAP.getIntValue();
			}
				
		}
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(int pushStatus) {
		this.pushStatus = pushStatus;
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

	public long getPublishAdminId() {
		return publishAdminId;
	}

	public void setPublishAdminId(long publishAdminId) {
		this.publishAdminId = publishAdminId;
	}

	public long getGrantAdminId() {
		return grantAdminId;
	}

	public void setGrantAdminId(long grantAdminId) {
		this.grantAdminId = grantAdminId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public int getGetWay() {
		return getWay;
	}

	public void setGetWay(int getWay) {
		this.getWay = getWay;
	}

	public List<String> getCouponUseTips() {
		return couponUseTips;
	}

	public void setCouponUseTips(List<String> couponUseTips) {
		this.couponUseTips = couponUseTips;
	}

	public String getRelatedUrl() {
		return relatedUrl;
	}

	public void setRelatedUrl(String relatedUrl) {
		this.relatedUrl = relatedUrl;
	}

	public double getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(double actualDiscount) {
		this.actualDiscount = actualDiscount;
	}

	public double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getValidityStartTimeStr() {
		
		if(validityStartTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(validityStartTime, UtilDate.simple);
		
	}


	public String getValidityEndTimeStr() {
		
		if(validityEndTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(validityEndTime, UtilDate.simple);
	
}
//	public Object getRangeDetail() {
//		if(rangeContent != null) {
//			return JSON.parse(rangeContent);
//		}
//		return null;
//	}

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
