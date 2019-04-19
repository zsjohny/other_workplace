package com.jiuy.wxaproxy.common.system.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiuyuan
 * @since 2017-07-11
 */
@TableName("wxaproxy_test")
public class Test extends Model<Test> {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Test{" + "id=" + id + ", value=" + value + "}";
	}
}
