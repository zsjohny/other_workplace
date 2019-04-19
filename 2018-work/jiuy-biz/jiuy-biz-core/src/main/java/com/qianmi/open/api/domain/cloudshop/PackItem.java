package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 包裹清单
 *
 * @author auto
 * @since 2.0
 */
public class PackItem extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品发货数量
	 */
	@ApiField("num")
	private Integer num;

	/**
	 * 商品单编号
	 */
	@ApiField("oid")
	private String oid;

	/**
	 * 商家外部编码
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 包裹编号
	 */
	@ApiField("pack_id")
	private String packId;

	/**
	 * 包裹清单编号
	 */
	@ApiField("pack_item_id")
	private String packItemId;

	/**
	 * 订单编号
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

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

	public String getPackId() {
		return this.packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getPackItemId() {
		return this.packItemId;
	}
	public void setPackItemId(String packItemId) {
		this.packItemId = packItemId;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}