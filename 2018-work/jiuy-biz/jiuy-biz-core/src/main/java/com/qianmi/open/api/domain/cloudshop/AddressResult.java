package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 卖家地址库
 *
 * @author auto
 * @since 2.0
 */
public class AddressResult extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 详细地址
	 */
	@ApiField("addr")
	private String addr;

	/**
	 * 区域ID
	 */
	@ApiField("area_id")
	private String areaId;

	/**
	 * 是否默认退货地址 0不是 1是
	 */
	@ApiField("cancel_def")
	private Integer cancelDef;

	/**
	 * 市
	 */
	@ApiField("city")
	private String city;

	/**
	 * 卖家地址库编号
	 */
	@ApiField("contact_id")
	private String contactId;

	/**
	 * 联系人姓名
	 */
	@ApiField("contact_name")
	private String contactName;

	/**
	 * 区
	 */
	@ApiField("country")
	private String country;

	/**
	 * 是否是收货地址 0不是 1是
	 */
	@ApiField("delivery_def")
	private Integer deliveryDef;

	/**
	 * 是否默认取货地址 0不是 1是
	 */
	@ApiField("get_def")
	private Integer getDef;

	/**
	 * 是否新增成功
	 */
	@ApiField("is_success")
	private boolean isSuccess;

	/**
	 * 备注
	 */
	@ApiField("memo")
	private String memo;

	/**
	 * 手机号，手机、电话必须有一个
	 */
	@ApiField("mobile")
	private String mobile;

	/**
	 * 最后修改时间
	 */
	@ApiField("modified")
	private String modified;

	/**
	 * 电话号, 手机、电话必须有一个
	 */
	@ApiField("phone")
	private String phone;

	/**
	 * 省
	 */
	@ApiField("province")
	private String province;

	/**
	 * 卖家编号, A开头
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 是否默认发货地址 0不是 1是
	 */
	@ApiField("send_def")
	private Integer sendDef;

	/**
	 * 邮政编码
	 */
	@ApiField("zip")
	private String zip;

	public String getAddr() {
		return this.addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAreaId() {
		return this.areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public Integer getCancelDef() {
		return this.cancelDef;
	}
	public void setCancelDef(Integer cancelDef) {
		this.cancelDef = cancelDef;
	}

	public String getCity() {
		return this.city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getContactId() {
		return this.contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return this.contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCountry() {
		return this.country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getDeliveryDef() {
		return this.deliveryDef;
	}
	public void setDeliveryDef(Integer deliveryDef) {
		this.deliveryDef = deliveryDef;
	}

	public Integer getGetDef() {
		return this.getDef;
	}
	public void setGetDef(Integer getDef) {
		this.getDef = getDef;
	}

	public boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getModified() {
		return this.modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return this.province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public Integer getSendDef() {
		return this.sendDef;
	}
	public void setSendDef(Integer sendDef) {
		this.sendDef = sendDef;
	}

	public String getZip() {
		return this.zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

}