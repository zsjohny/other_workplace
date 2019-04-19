package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 商品规格值属性
 *
 * @author auto
 * @since 2.0
 */
public class PropVal extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 规格值id
	 */
	@ApiField("vid")
	private String vid;

	/**
	 * 规格值
	 */
	@ApiField("vname")
	private String vname;

	public String getVid() {
		return this.vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getVname() {
		return this.vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}

}