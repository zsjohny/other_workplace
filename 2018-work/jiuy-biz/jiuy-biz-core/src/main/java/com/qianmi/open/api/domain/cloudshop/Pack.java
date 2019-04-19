package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 包裹信息，云订货订单发货会产生包裹
 *
 * @author auto
 * @since 2.0
 */
public class Pack extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 卖家编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 是否发货成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 买家编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 运单号，真实的一个物流公司的运单号码
	 */
	@ApiField("out_sid")
	private String outSid;

	/**
	 * 包裹编号
	 */
	@ApiField("pack_id")
	private String packId;

	/**
	 * 包裹清单
	 */
	@ApiListField("pack_items")
	@ApiField("pack_item")
	private List<PackItem> packItems;

	/**
	 * 包裹状态，0：作废，1：已打包待发货，2：已发货待签收，3：已签收
	 */
	@ApiField("pack_status")
	private Integer packStatus;

	/**
	 * 打包时间
	 */
	@ApiField("pack_time")
	private String packTime;

	/**
	 * 物流费用，单位：元，精确到2位小数
	 */
	@ApiField("post_fee")
	private String postFee;

	/**
	 * 买家收货时间
	 */
	@ApiField("receive_time")
	private String receiveTime;

	/**
	 * 发货时间
	 */
	@ApiField("send_time")
	private String sendTime;

	/**
	 * 物流公司编号
	 */
	@ApiField("ship_company_id")
	private String shipCompanyId;

	/**
	 * 物流公司名称
	 */
	@ApiField("ship_company_name")
	private String shipCompanyName;

	/**
	 * 发货方式编号
	 */
	@ApiField("ship_type_id")
	private String shipTypeId;

	/**
	 * 发货方式名称
	 */
	@ApiField("ship_type_name")
	private String shipTypeName;

	/**
	 * 订单编号
	 */
	@ApiField("tid")
	private String tid;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMemberId() {
		return this.memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOutSid() {
		return this.outSid;
	}
	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}

	public String getPackId() {
		return this.packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}

	public List<PackItem> getPackItems() {
		return this.packItems;
	}
	public void setPackItems(List<PackItem> packItems) {
		this.packItems = packItems;
	}

	public Integer getPackStatus() {
		return this.packStatus;
	}
	public void setPackStatus(Integer packStatus) {
		this.packStatus = packStatus;
	}

	public String getPackTime() {
		return this.packTime;
	}
	public void setPackTime(String packTime) {
		this.packTime = packTime;
	}

	public String getPostFee() {
		return this.postFee;
	}
	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}

	public String getReceiveTime() {
		return this.receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getSendTime() {
		return this.sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getShipCompanyId() {
		return this.shipCompanyId;
	}
	public void setShipCompanyId(String shipCompanyId) {
		this.shipCompanyId = shipCompanyId;
	}

	public String getShipCompanyName() {
		return this.shipCompanyName;
	}
	public void setShipCompanyName(String shipCompanyName) {
		this.shipCompanyName = shipCompanyName;
	}

	public String getShipTypeId() {
		return this.shipTypeId;
	}
	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}

	public String getShipTypeName() {
		return this.shipTypeName;
	}
	public void setShipTypeName(String shipTypeName) {
		this.shipTypeName = shipTypeName;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

}