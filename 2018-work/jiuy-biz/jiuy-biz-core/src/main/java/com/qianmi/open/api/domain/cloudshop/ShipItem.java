package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 发货商品清单
 *
 * @author auto
 * @since 2.0
 */
public class ShipItem extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * sku名称
	 */
	@ApiField("item_name")
	private String itemName;

	/**
	 * 商品清单编号
	 */
	@ApiField("list_id")
	private String listId;

	/**
	 * 商品数量
	 */
	@ApiField("num")
	private Integer num;

	/**
	 * 物流单编号
	 */
	@ApiField("order_code")
	private String orderCode;

	/**
	 * sku商家编号
	 */
	@ApiField("outer_id")
	private String outerId;

	public String getItemName() {
		return this.itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getListId() {
		return this.listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}

	public Integer getNum() {
		return this.num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

	public String getOrderCode() {
		return this.orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOuterId() {
		return this.outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

}