package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 邮寄信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-30
 */
@TableName("store_ExpressInfo")
public class StoreExpressInfo extends Model<StoreExpressInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 订单表OrderNo
     */
	private Long OrderNo;
    /**
     * 快递提供商
     */
	private String ExpressSupplier;
    /**
     * 快递订单号
     */
	private String ExpressOrderNo;
    /**
     * 快递信息更新时间
     */
	private Long ExpressUpdateTime;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 状态
     */
	private Integer Status;
	/**
     * 物流信息内容
     */
	private String ExpressInfo;
	
	
	

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public String getExpressSupplier() {
		return ExpressSupplier;
	}

	public void setExpressSupplier(String ExpressSupplier) {
		this.ExpressSupplier = ExpressSupplier;
	}

	public String getExpressOrderNo() {
		return ExpressOrderNo;
	}

	public void setExpressOrderNo(String ExpressOrderNo) {
		this.ExpressOrderNo = ExpressOrderNo;
	}

	public Long getExpressUpdateTime() {
		return ExpressUpdateTime;
	}

	public void setExpressUpdateTime(Long ExpressUpdateTime) {
		this.ExpressUpdateTime = ExpressUpdateTime;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}
	public String getExpressInfo() {
		return ExpressInfo;
	}
	
	public void setExpressInfo(String expressInfo) {
		this.ExpressInfo = expressInfo;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}


}
