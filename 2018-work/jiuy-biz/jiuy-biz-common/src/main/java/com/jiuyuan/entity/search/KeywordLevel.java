/**
 * 
 */
package com.jiuyuan.entity.search;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2016年9月27日 上午10:09:20
*/
public class KeywordLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3388637659161220981L;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	private String name;
	private String weight;
}