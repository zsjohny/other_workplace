package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 代理销售订单
 * </p>
 *
 * @author 赵兴林
 * @since 2018-04-07
 */
@TableName("wxaproxy_order")
public class ProxyOrder extends Model<ProxyOrder> {

    private static final long serialVersionUID = 1L;
    public final static int orderState_new = 0;// 订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    public final static int orderState_auditing = 1;// 订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    public final static int orderState_finish = 2;// 订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    public final static int orderState_close = 3;// 订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
    
    
    
    public String buildOrderStateName() {
    	String orderStateName = "";
		if(this.orderState == orderState_new){
			orderStateName = "新申请";
		}else if(this.orderState == orderState_auditing){
			orderStateName = "受理中";
		}else if(this.orderState == orderState_finish){
			orderStateName = "已完成";
		}else if(this.orderState == orderState_close){
			orderStateName = "已关闭";
		}
		return orderStateName;
	}

    
    
    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
//    /**
//     * 订单编号
//     */
//	@TableField("proxy_order_no")
//	private String proxyOrderNo;
    /**
     * 订单状态：新申请(0)、受理中(1)、已完成(2)、已关闭(3)
     */
	@TableField("order_state")
	private Integer orderState;
    /**
     * 申请人姓名
     */
	@TableField("apply_full_name")
	private String applyFullName;
    /**
     * 申请人手机号
     */
	@TableField("apply_phone")
	private String applyPhone;
    /**
     * 申请开通服务产品ID
     */
	@TableField("proxy_product_id")
	private Long proxyProductId;
    /**
     * 申请开通服务产品名称
     */
	@TableField("proxy_product_name")
	private String proxyProductName;
    /**
     * 申请开通服务数量
     */
	@TableField("proxy_product_count")
	private Integer proxyProductCount;
    /**
     * 代理商ID
     */
	@TableField("proxy_user_id")
	private Long proxyUserId;
	  /**
     * 代理商名称
     */
	@TableField("proxy_user_name")
	private String proxyUserName;
    /**
     * 代理商编号
     */
	@TableField("proxy_user_no")
	private String proxyUserNo;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
//	public String getProxyOrderNo() {
//		return proxyOrderNo;
//	}
//
//	public void setProxyOrderNo(String proxyOrderNo) {
//		this.proxyOrderNo = proxyOrderNo;
//	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getApplyFullName() {
		return applyFullName;
	}

	public void setApplyFullName(String applyFullName) {
		this.applyFullName = applyFullName;
	}

	public String getApplyPhone() {
		return applyPhone;
	}

	public void setApplyPhone(String applyPhone) {
		this.applyPhone = applyPhone;
	}

	public Long getProxyProductId() {
		return proxyProductId;
	}

	public void setProxyProductId(Long proxyProductId) {
		this.proxyProductId = proxyProductId;
	}

	public String getProxyProductName() {
		return proxyProductName;
	}

	public void setProxyProductName(String proxyProductName) {
		this.proxyProductName = proxyProductName;
	}

	public Integer getProxyProductCount() {
		return proxyProductCount;
	}

	public void setProxyProductCount(Integer proxyProductCount) {
		this.proxyProductCount = proxyProductCount;
	}

	public Long getProxyUserId() {
		return proxyUserId;
	}

	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}

	public String getProxyUserNo() {
		return proxyUserNo;
	}

	public void setProxyUserNo(String proxyUserNo) {
		this.proxyUserNo = proxyUserNo;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	

	
}
