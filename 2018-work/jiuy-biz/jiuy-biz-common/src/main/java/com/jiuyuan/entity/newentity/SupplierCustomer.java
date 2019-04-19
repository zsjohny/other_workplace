package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 供应商客户表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@TableName("supplier_customer")
public class SupplierCustomer extends Model<SupplierCustomer> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 客户名称：店铺名称
     */
	private String businessName;
    /**
     * 客户姓名：法人
     */
	@TableField("customer_name")
	private String customerName;
	/**
	 * 供应商Id
	 */
	@TableField("supplier_id")
	private Long supplierId;
	/**
	 * 门店id
	 */
	@TableField("store_id")
	private Long storeId;
    /**
     * 备注名
     */
	@TableField("remark_name")
	private String remarkName;
    /**
     * 手机号码
     */
	@TableField("phone_number")
	private String phoneNumber;
    /**
     * 分组Id
     */
	@TableField("group_id")
	private Long groupId;
    /**
     * 所在省份
     */
	private String province;
    /**
     * 所在城市
     */
	private String city;
    /**
     * 所在区县
     */
	private String businessAddress;
    /**
     *   状态  0：新客户   1：老客户
     */
	@TableField("customer_type")
	private Integer customerType;
    /**
     *   状态  -1：删除   0：正常
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
