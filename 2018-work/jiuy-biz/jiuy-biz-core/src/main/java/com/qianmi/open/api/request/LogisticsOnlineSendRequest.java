package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.LogisticsOnlineSendResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.logistics.online.send request
 *
 * @author auto
 * @since 1.0
 */
public class LogisticsOnlineSendRequest implements QianmiRequest<LogisticsOnlineSendResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 退货地址编号
	 */
	private String cancelId;

	/** 
	 * 卖家物流公司编号
	 */
	private String companyId;

	/** 
	 * 运单号
	 */
	private String outSid;

	/** 
	 * 物流费用
	 */
	private String postFee;

	/** 
	 * 详细地址
	 */
	private String reciverAddress;

	/** 
	 * 收件人城市
	 */
	private String reciverCity;

	/** 
	 * 收件人所在区县
	 */
	private String reciverDistrict;

	/** 
	 * 收件人手机, 手机、电话至少有一个
	 */
	private String reciverMobile;

	/** 
	 * 收件人姓名
	 */
	private String reciverName;

	/** 
	 * 收件人电话, 手机、电话至少有一个
	 */
	private String reciverPhone;

	/** 
	 * 收件人省份
	 */
	private String reciverState;

	/** 
	 * 收件地址邮编
	 */
	private String reciverZip;

	/** 
	 * 卖家编号，A开头
	 */
	private String sellerNick;

	/** 
	 * 卖家发货备注
	 */
	private String sellerRemark;

	/** 
	 * 发货地址编号
	 */
	private String senderId;

	/** 
	 * 发货方式编号
	 */
	private String shipTypeId;

	/** 
	 * 发货方式名称
	 */
	private String shipTypeName;

	/** 
	 * 千米网交易id
	 */
	private String tid;

	public void setCancelId(String cancelId) {
		this.cancelId = cancelId;
	}
	public String getCancelId() {
		return this.cancelId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyId() {
		return this.companyId;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}
	public String getOutSid() {
		return this.outSid;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	public String getPostFee() {
		return this.postFee;
	}

	public void setReciverAddress(String reciverAddress) {
		this.reciverAddress = reciverAddress;
	}
	public String getReciverAddress() {
		return this.reciverAddress;
	}

	public void setReciverCity(String reciverCity) {
		this.reciverCity = reciverCity;
	}
	public String getReciverCity() {
		return this.reciverCity;
	}

	public void setReciverDistrict(String reciverDistrict) {
		this.reciverDistrict = reciverDistrict;
	}
	public String getReciverDistrict() {
		return this.reciverDistrict;
	}

	public void setReciverMobile(String reciverMobile) {
		this.reciverMobile = reciverMobile;
	}
	public String getReciverMobile() {
		return this.reciverMobile;
	}

	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}
	public String getReciverName() {
		return this.reciverName;
	}

	public void setReciverPhone(String reciverPhone) {
		this.reciverPhone = reciverPhone;
	}
	public String getReciverPhone() {
		return this.reciverPhone;
	}

	public void setReciverState(String reciverState) {
		this.reciverState = reciverState;
	}
	public String getReciverState() {
		return this.reciverState;
	}

	public void setReciverZip(String reciverZip) {
		this.reciverZip = reciverZip;
	}
	public String getReciverZip() {
		return this.reciverZip;
	}

	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}
	public String getSellerNick() {
		return this.sellerNick;
	}

	public void setSellerRemark(String sellerRemark) {
		this.sellerRemark = sellerRemark;
	}
	public String getSellerRemark() {
		return this.sellerRemark;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderId() {
		return this.senderId;
	}

	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}
	public String getShipTypeId() {
		return this.shipTypeId;
	}

	public void setShipTypeName(String shipTypeName) {
		this.shipTypeName = shipTypeName;
	}
	public String getShipTypeName() {
		return this.shipTypeName;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTid() {
		return this.tid;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.logistics.online.send";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("cancel_id", this.cancelId);
		txtParams.put("company_id", this.companyId);
		txtParams.put("out_sid", this.outSid);
		txtParams.put("post_fee", this.postFee);
		txtParams.put("reciver_address", this.reciverAddress);
		txtParams.put("reciver_city", this.reciverCity);
		txtParams.put("reciver_district", this.reciverDistrict);
		txtParams.put("reciver_mobile", this.reciverMobile);
		txtParams.put("reciver_name", this.reciverName);
		txtParams.put("reciver_phone", this.reciverPhone);
		txtParams.put("reciver_state", this.reciverState);
		txtParams.put("reciver_zip", this.reciverZip);
		txtParams.put("seller_nick", this.sellerNick);
		txtParams.put("seller_remark", this.sellerRemark);
		txtParams.put("sender_id", this.senderId);
		txtParams.put("ship_type_id", this.shipTypeId);
		txtParams.put("ship_type_name", this.shipTypeName);
		txtParams.put("tid", this.tid);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<LogisticsOnlineSendResponse> getResponseClass() {
		return LogisticsOnlineSendResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(companyId, "companyId");
		RequestCheckUtils.checkNotEmpty(outSid, "outSid");
		RequestCheckUtils.checkNotEmpty(sellerNick, "sellerNick");
		RequestCheckUtils.checkNotEmpty(shipTypeId, "shipTypeId");
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}