package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Charlie
 * @since 2018-08-14
 */
@TableName("yjj_super_order")
public class SuperOrder extends Model<SuperOrder>{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId( value = "id", type = IdType.AUTO )
	private Long id;
	/**
	 * app订单id
	 */
	@TableField( "store_order_no" )
	private String storeOrderNo;
	@TableField( "create_time" )
	private Date createTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreOrderNo() {
		return storeOrderNo;
	}

	public void setStoreOrderNo(String storeOrderNo) {
		this.storeOrderNo = storeOrderNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}