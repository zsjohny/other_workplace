package com.store.entity.coupon;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 优惠券模板表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-14
 */
@TableName("shop_coupon_template")
public class ShopCouponTemplate extends Model<ShopCouponTemplate> {
	/*一个门店最多可用优惠券数据*/
	public static final int canUseMaxCount = 30;
	
	
	   //状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
	public static final int status_del = -1;
	public static final int status_normal =0;
	public static final int status_stop = 1;
	public static final int status_use_up= 2;
	public static final int status_lose_efficacy = 3;

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 标题
     */
	private String name;
	 /**
     * 是否填写标题：0未填写，1已填写
     */
	@TableField("fillin_name")
	private int fillinName;
    /**
     * 面值 元
     */
	@TableField("money")
	private Double money;
	
    /**
     * 限额 满多少可以使用
     */
	@TableField("limit_money")
	private Double limitMoney;
	
    /**
     * 领取量
     */
	@TableField("get_count")
	private Integer getCount;
	
	
    /**
     * 使用量
     */
	@TableField("used_count")
	private Integer usedCount;
    /**
     * 可用量（已废弃）
     */
	@TableField("available_count")
	private Integer availableCount;
    /**
     * 发放量
     */
	@TableField("grant_count")
	private Integer grantCount;
    /**
     * 发行量（已废弃）
     */
	@TableField("publish_count")
	private Integer publishCount;
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
     * 有效开始时间
     */
	@TableField(exist = false)
	private String validityStartTimeStr;
	/**
     * 有效结束时间
     */
	@TableField(exist = false)
	private String validityEndTimeStr;
	
	
    /**
     * 状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
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

	
	

	public int getFillinName() {
		return fillinName;
	}

	public void setFillinName(int fillinName) {
		this.fillinName = fillinName;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public Integer getGetCount() {
		return getCount;
	}

	public void setGetCount(Integer getCount) {
		this.getCount = getCount;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Integer getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}

	public Integer getGrantCount() {
		return grantCount;
	}

	public void setGrantCount(Integer grantCount) {
		this.grantCount = grantCount;
	}

	public Integer getPublishCount() {
		return publishCount;
	}

	public void setPublishCount(Integer publishCount) {
		this.publishCount = publishCount;
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
	
	public String getValidityStartTimeStr() {
		return validityStartTimeStr;
	}

	public void setValidityStartTimeStr(String validityStartTimeStr) {
		this.validityStartTimeStr = validityStartTimeStr;
	}

	public String getValidityEndTimeStr() {
		return validityEndTimeStr;
	}

	public void setValidityEndTimeStr(String validityEndTimeStr) {
		this.validityEndTimeStr = validityEndTimeStr;
	}
	
	/**
	 * 验证是否可领取
	 * @return 
	 */
	public boolean checkIsCanGet(long time) {
		if(time < validityEndTime ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 验证是否可领取
	 * @return 
	 */
	public boolean checkIsCanGet() {
		long time = System.currentTimeMillis();
		return checkIsCanGet(time);
	}


	/**
	 * 验证是否失效
	 * @return 
	 */
	public boolean checkIsLoseEfficacy() {
		long time = System.currentTimeMillis();
		return checkIsLoseEfficacy(time);
	}
	
	/**
	 * 验证是否失效
	 * @return 
	 */
	public boolean checkIsLoseEfficacy(long time) {
		if(time > validityStartTime && time < validityEndTime ){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 验证是否已领完
	 * @return 
	 */
	public boolean checkIsUseUp() {
		//发放量大于领取量
		if(grantCount > getCount){
			return false;	
		}
		return true;
	}

}
