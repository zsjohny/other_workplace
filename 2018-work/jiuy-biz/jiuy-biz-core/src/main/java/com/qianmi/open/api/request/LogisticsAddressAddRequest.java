package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.LogisticsAddressAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.logistics.address.add request
 *
 * @author auto
 * @since 1.0
 */
public class LogisticsAddressAddRequest implements QianmiRequest<LogisticsAddressAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 详细地址
	 */
	private String addr;

	/** 
	 * 是否默认退货地址，只有admin卖家有该字段，1-是，0否，选择此项(1)，将当前地址设为默认地址，撤消原默认地址
	 */
	private String cancelDef;

	/** 
	 * 市
	 */
	private String city;

	/** 
	 * 联系人姓名
	 */
	private String contactName;

	/** 
	 * 区
	 */
	private String country;

	/** 
	 * 是否是收货地址 1-是，0-否，选择此项(1)，将当前地址设为默认地址，撤消原默认地址
	 */
	private String deliveryDef;

	/** 
	 * 卖家：表示是否默认提货地址，买家：是否默认收货地址，1-是，0-否，选择此项(1)，将当前地址设为默认地址，撤消原默认地址
	 */
	private String getDef;

	/** 
	 * 备注
	 */
	private String memo;

	/** 
	 * 手机号码（手机、固话必须有一个）
	 */
	private String mobile;

	/** 
	 * 固定电话（固话、手机必须有一个）
	 */
	private String phone;

	/** 
	 * 省
	 */
	private String province;

	/** 
	 * 是否默认发货地址，只有admin卖家有该字段，1-是，0-否，选择此项(1)，将当前地址设为默认地址，撤消原默认地址
	 */
	private String sendDef;

	/** 
	 * 邮政编码
	 */
	private String zip;

	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAddr() {
		return this.addr;
	}

	public void setCancelDef(String cancelDef) {
		this.cancelDef = cancelDef;
	}
	public String getCancelDef() {
		return this.cancelDef;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return this.city;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactName() {
		return this.contactName;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountry() {
		return this.country;
	}

	public void setDeliveryDef(String deliveryDef) {
		this.deliveryDef = deliveryDef;
	}
	public String getDeliveryDef() {
		return this.deliveryDef;
	}

	public void setGetDef(String getDef) {
		this.getDef = getDef;
	}
	public String getGetDef() {
		return this.getDef;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return this.memo;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile() {
		return this.mobile;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return this.phone;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvince() {
		return this.province;
	}

	public void setSendDef(String sendDef) {
		this.sendDef = sendDef;
	}
	public String getSendDef() {
		return this.sendDef;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getZip() {
		return this.zip;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.logistics.address.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("addr", this.addr);
		txtParams.put("cancel_def", this.cancelDef);
		txtParams.put("city", this.city);
		txtParams.put("contact_name", this.contactName);
		txtParams.put("country", this.country);
		txtParams.put("delivery_def", this.deliveryDef);
		txtParams.put("get_def", this.getDef);
		txtParams.put("memo", this.memo);
		txtParams.put("mobile", this.mobile);
		txtParams.put("phone", this.phone);
		txtParams.put("province", this.province);
		txtParams.put("send_def", this.sendDef);
		txtParams.put("zip", this.zip);
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

	public Class<LogisticsAddressAddResponse> getResponseClass() {
		return LogisticsAddressAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(addr, "addr");
		RequestCheckUtils.checkNotEmpty(city, "city");
		RequestCheckUtils.checkNotEmpty(contactName, "contactName");
		RequestCheckUtils.checkNotEmpty(country, "country");
		RequestCheckUtils.checkNotEmpty(province, "province");
		RequestCheckUtils.checkNotEmpty(zip, "zip");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}