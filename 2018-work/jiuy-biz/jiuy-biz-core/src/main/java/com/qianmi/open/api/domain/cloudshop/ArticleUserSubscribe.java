package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 用户订购信息
 *
 * @author auto
 * @since 2.0
 */
public class ArticleUserSubscribe extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 订购关系到期时间
	 */
	@ApiField("deadline")
	private String deadline;

	/**
	 * 收费项目代码
	 */
	@ApiField("item_code")
	private String itemCode;

	/**
	 * 收费项目名称
	 */
	@ApiField("item_name")
	private String itemName;

	public String getDeadline() {
		return this.deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getItemCode() {
		return this.itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}