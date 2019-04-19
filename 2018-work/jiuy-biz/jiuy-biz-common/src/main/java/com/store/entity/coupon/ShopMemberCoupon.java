package com.store.entity.coupon;

import java.io.Serializable;
import java.util.Calendar;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-14
 */
@TableName("shop_member_coupon")
public class ShopMemberCoupon extends Model<ShopMemberCoupon> {

    private static final long serialVersionUID = 1L;
    //状态:-1：删除，0：正常，1：使用
    public static final int status_del = -1;
	public static final int status_normal =0;
	public static final int status_used = 1;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 会员Id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 会员昵称
     */
	@TableField("member_nicheng")
	private String memberNicheng;
    /**
     * 核销操作员Id
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 核销时间
     */
	@TableField("check_time")
	private Long checkTime;
    /**
     * 核销金额 元
     */
	@TableField("check_money")
	private Double checkMoney;
    /**
     * 优惠券模板Id
     */
	@TableField("coupon_template_id")
	private Long couponTemplateId;
    /**
     * 标题
     */
	private String name;
    /**
     * 面值 元
     */
	private Double money;
    /**
     * 限额 满多少可以使用
     */
	@TableField("limit_money")
	private Double limitMoney;
    /**
     * 有效开始时间
     */
	@TableField("validity_start_time")
	private Long validityStartTime;
    /**
     * 有效结束时间
     */
	@TableField("validity_end_time")
	private Long validityEndTime;
    /**
     * 状态:-1：删除，0：正常，1：使用
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberNicheng() {
		return memberNicheng;
	}

	public void setMemberNicheng(String memberNicheng) {
		this.memberNicheng = memberNicheng;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Long checkTime) {
		this.checkTime = checkTime;
	}

	public Double getCheckMoney() {
		return checkMoney;
	}

	public void setCheckMoney(Double checkMoney) {
		this.checkMoney = checkMoney;
	}
	
	public String getStringCheckMoney() {
		return getStringMoney(this.checkMoney);
	}

	public Long getCouponTemplateId() {
		return couponTemplateId;
	}

	public void setCouponTemplateId(Long couponTemplateId) {
		this.couponTemplateId = couponTemplateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Double getMoney() {
		return this.money;
	}

	public String getStringMoney() {
		return getStringMoney(this.money);
	}
	
	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getLimitMoney() {
		return limitMoney;
	}
	
	public String getStringLimitMoney() {
		return getStringMoney(this.limitMoney);
	}
	
	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public Long getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(Long validityStartTime) {
		this.validityStartTime = validityStartTime;
	}

	public Long getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(Long validityEndTime) {
		this.validityEndTime = validityEndTime;
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
	
	/**
	 * 判断是否已过期
	 * @return
	 */
	public boolean isOverdue(){
		Calendar calendar = Calendar.getInstance();  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0); 
		return this.validityEndTime<calendar.getTimeInMillis();
	}
	
	/**
	 * 判断是否还未开始
	 * @return
	 */
	public boolean isNotBegin(){
		Calendar calendar = Calendar.getInstance();  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0); 
		return this.validityStartTime>=(calendar.getTimeInMillis()+24*60*60*1000);
	}

	@Override
	public String toString() {
		return "ShopMemberCoupon [id=" + id + ", storeId=" + storeId + ", memberId=" + memberId + ", memberNicheng="
				+ memberNicheng + ", adminId=" + adminId + ", checkTime=" + checkTime + ", checkMoney=" + checkMoney
				+ ", couponTemplateId=" + couponTemplateId + ", name=" + name + ", money=" + money + ", limitMoney="
				+ limitMoney + ", validityStartTime=" + validityStartTime + ", validityEndTime=" + validityEndTime
				+ ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	/**
	 * 去除整数的小数点的0
	 * @param m
	 * @return
	 */
	public String getStringMoney(Double m) {
		if(m.intValue()-m==0){//判断是否符合取整条件  
		    return m.intValue()+"";
		}else{  
			return String.format("%.2f", m);
		}
	}

}