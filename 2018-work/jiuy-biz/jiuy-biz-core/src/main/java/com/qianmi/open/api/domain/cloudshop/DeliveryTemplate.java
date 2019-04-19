package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 运费模板
 *
 * @author auto
 * @since 2.0
 */
public class DeliveryTemplate extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 模板的发货地区
	 */
	@ApiField("address")
	private String address;

	/**
	 * 商家A编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 1-卖家承担运费，0-买家承担运费
	 */
	@ApiField("assumer")
	private Integer assumer;

	/**
	 * 模板的发货地区ID，按省市区  取设置的最小单元的 area_id
	 */
	@ApiField("consign_area_id")
	private String consignAreaId;

	/**
	 * 模板创建时间
	 */
	@ApiField("created")
	private String created;

	/**
	 * 运费模板中运费详细信息对象，包含默认运费和指定地区运费
	 */
	@ApiListField("fee_list")
	@ApiField("delivery_fee")
	private List<DeliveryFee> feeList;

	/**
	 * 是否是默认模板 1是 0否
	 */
	@ApiField("is_def")
	private String isDef;

	/**
	 * 操作是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 是否是系统模板 1是 0否
	 */
	@ApiField("is_sys")
	private String isSys;

	/**
	 * 模板最后修改时间
	 */
	@ApiField("modified")
	private String modified;

	/**
	 * 模板名称  2-20长度
	 */
	@ApiField("name")
	private String name;

	/**
	 * 是否支持区域限售，1-支持，0-不支持，如果支持，商品只能在设置了运费的指定地区销售
	 */
	@ApiField("sale_area_limit")
	private Integer saleAreaLimit;

	/**
	 * 模板编号
	 */
	@ApiField("template_id")
	private Integer templateId;

	/**
	 * 计价方式：1-按重计价，0-按件计价
	 */
	@ApiField("valuation")
	private Integer valuation;

	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Integer getAssumer() {
		return this.assumer;
	}
	public void setAssumer(Integer assumer) {
		this.assumer = assumer;
	}

	public String getConsignAreaId() {
		return this.consignAreaId;
	}
	public void setConsignAreaId(String consignAreaId) {
		this.consignAreaId = consignAreaId;
	}

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public List<DeliveryFee> getFeeList() {
		return this.feeList;
	}
	public void setFeeList(List<DeliveryFee> feeList) {
		this.feeList = feeList;
	}

	public String getIsDef() {
		return this.isDef;
	}
	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getIsSys() {
		return this.isSys;
	}
	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}

	public String getModified() {
		return this.modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getSaleAreaLimit() {
		return this.saleAreaLimit;
	}
	public void setSaleAreaLimit(Integer saleAreaLimit) {
		this.saleAreaLimit = saleAreaLimit;
	}

	public Integer getTemplateId() {
		return this.templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getValuation() {
		return this.valuation;
	}
	public void setValuation(Integer valuation) {
		this.valuation = valuation;
	}

}