package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 运费模板中运费详细信息，包含默认运费和指定地区运费
 *
 * @author auto
 * @since 2.0
 */
public class DeliveryFee extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 增费   0.00至999.99  最多包含2位小数
	 */
	@ApiField("add_fee")
	private String addFee;

	/**
	 * 增费标准 1-9999内的整数
	 */
	@ApiField("add_standard")
	private String addStandard;

	/**
	 * 邮费子项涉及的地区编码,多个地区用逗号连接成串; 例如086，310000，320000，330000
	 */
	@ApiField("destination")
	private String destination;

	/**
	 * 发货方式编号 self:自提,express:快递,logistics:物流,virtual:虚拟发货,post:平邮,ems:EMS
	 */
	@ApiField("ship_type_id")
	private String shipTypeId;

	/**
	 * 首费 0.00至999.99  最多包含2位小数
	 */
	@ApiField("start_fee")
	private String startFee;

	/**
	 * 首费标准   不管按重计价还是按件计价   收费标准有效范围：1-9999内的整数
	 */
	@ApiField("start_standard")
	private String startStandard;

	public String getAddFee() {
		return this.addFee;
	}
	public void setAddFee(String addFee) {
		this.addFee = addFee;
	}

	public String getAddStandard() {
		return this.addStandard;
	}
	public void setAddStandard(String addStandard) {
		this.addStandard = addStandard;
	}

	public String getDestination() {
		return this.destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getShipTypeId() {
		return this.shipTypeId;
	}
	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}

	public String getStartFee() {
		return this.startFee;
	}
	public void setStartFee(String startFee) {
		this.startFee = startFee;
	}

	public String getStartStandard() {
		return this.startStandard;
	}
	public void setStartStandard(String startStandard) {
		this.startStandard = startStandard;
	}

}