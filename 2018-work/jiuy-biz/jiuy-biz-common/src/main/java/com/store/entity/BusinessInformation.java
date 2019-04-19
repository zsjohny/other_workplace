package com.store.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.jiuyuan.util.intercept.prevalid.annotation.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;


/**
 * <p>
 * 商家信息表
 * </p>
 *
 * @author Hyf
 * @since 2018-08-15
 */
@TableName("yjj_business_information")
public class BusinessInformation extends Model<BusinessInformation> {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家用户id
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 商家名称
     */
	private String name;
    /**
     * 商家地址
     */
	private String address;
    /**
     * 商家电话
     */
	private String phone;
    /**
     * 门店介绍
     */
	private String introduction;
	/**
	 *创建时间
	 * @return
	 */
	@TableField("create_time")
	private String createTime;
	/**
	 *修改时间
	 * @return
	 */
	@TableField("update_time")
	private String updateTime;
	/**
	 *状态 是否删除 默认0正常 1删除
	 * @return
	 */
	@TableField("del_state")
	private Integer delState;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDelState() {
		return delState;
	}

	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
//		if (StringUtils.isEmpty(name)){
//			throw new RuntimeException("商家名称不能为空");
//		}
		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getAddress() {
//		if (StringUtils.isEmpty(address)){
//			throw new RuntimeException("商家地址不能为空");
//		}
		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getPhone() {
//		if (StringUtils.isEmpty(phone)){
//			throw new RuntimeException("商家手机号不能为空");
//		}
		return phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BusinessInformation{" +
				"id=" + id +
				", userId=" + userId +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", introduction='" + introduction + '\'' +
				", createTime='" + createTime + '\'' +
				", updateTime='" + updateTime + '\'' +
				", delState=" + delState +
				'}';
	}
}
