package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 退货商品清单
 *
 * @author auto
 * @since 2.0
 */
public class ReturnedItem extends QianmiObject {

	private static final long serialVersionUID = 1L;

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
	 * 商品单号
	 */
	@ApiField("oid")
	private String oid;

	/**
	 * SKU商家编号
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 商品规格
	 */
	@ApiField("props_name")
	private String propsName;

	/**
	 * 退货单编号
	 */
	@ApiField("return_id")
	private String returnId;

	/**
	 * 商品名称
	 */
	@ApiField("title")
	private String title;

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

	public String getOid() {
		return this.oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOuterId() {
		return this.outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getPropsName() {
		return this.propsName;
	}
	public void setPropsName(String propsName) {
		this.propsName = propsName;
	}

	public String getReturnId() {
		return this.returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}