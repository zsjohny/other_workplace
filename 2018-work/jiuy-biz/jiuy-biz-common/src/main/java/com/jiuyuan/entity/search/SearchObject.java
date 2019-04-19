/**
 * 
 */
package com.jiuyuan.entity.search;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2016年9月27日 上午10:16:40
*/
public class SearchObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 912440642496171586L;
	
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public String getObject_name() {
		return object_name;
	}
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}
	public String getMatch_name() {
		return match_name;
	}
	public void setMatch_name(String match_name) {
		this.match_name = match_name;
	}
	private Float weight;
	private String object_name;
	private String match_name;
}