package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 商品图片
 *
 * @author auto
 * @since 2.0
 */
public class SkuImg extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 图片创建时间 时间格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("created")
	private String created;

	/**
	 * 图片id
	 */
	@ApiField("id")
	private String id;

	/**
	 * 图片放在第几张
	 */
	@ApiField("position")
	private String position;

	/**
	 * 图片链接地址
	 */
	@ApiField("url")
	private String url;

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPosition() {
		return this.position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}