package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 商品规格项属性
 *
 * @author auto
 * @since 2.0
 */
public class ItemProp extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 规格项id
	 */
	@ApiField("pid")
	private String pid;

	/**
	 * 规格项名称
	 */
	@ApiField("pname")
	private String pname;

	/**
	 * 规格值列表
	 */
	@ApiListField("prop_vals")
	@ApiField("prop_val")
	private List<PropVal> propVals;

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getPid() {
		return this.pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return this.pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}

	public List<PropVal> getPropVals() {
		return this.propVals;
	}
	public void setPropVals(List<PropVal> propVals) {
		this.propVals = propVals;
	}

}