package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 门店秒杀活动
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-24
 */
@TableName("store_second_buy_activity")
public class SecondBuyActivity extends Model<SecondBuyActivity> {

    private static final long serialVersionUID = 1L;
    
    public static final int DEL_STATE = -1;
    
    public static final int NORMAL_STATE = 0;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家号ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 活动标题
     */
	@TableField("activity_title")
	private String activityTitle;
    /**
     * 活动商品ID
     */
	@TableField("shop_product_id")
	private Long shopProductId;
    /**
     * 活动商品款号
     */
	@TableField("clothes_number")
	private String clothesNumber;
    /**
     * 活动商品名称
     */
	@TableField("shop_product_name")
	private String shopProductName;
    /**
     * 活动商品主图
     */
	@TableField("shop_product_mainimg")
	private String shopProductMainimg;
	/**
     * 活动商品橱窗图
     */
	@TableField("shop_product_showcase_imgs")
	private String shopProductShowcaseImgs;
    /**
     * 活动商品原价格
     */
	@TableField("activity_product_price")
	private Double activityProductPrice;
    /**
     * 活动价格
     */
	@TableField("activity_price")
	private Double activityPrice;
    /**
     * 活动商品数量
     */
	@TableField("activity_product_count")
	private Integer activityProductCount;
//	  /**
//     * 剩余活动商品数量(该字段已经废弃，可删除)
//     */
//	@TableField("surplus_activity_product_count")
//	private Integer surplusActivityProductCount;
	
//	/**
//     * 已卖出活动商品数量
//     */
//	@TableField("buy_activity_product_count")
//	private Integer buyActivityProductCount;
	
	
    /**
     * 活动有效开始时间
     */
	@TableField("activity_start_time")
	private Long activityStartTime;
    /**
     * 活动有效截止时间
     */
	@TableField("activity_end_time")
	private Long activityEndTime;
    /**
     * 活动手工结束时间：0表示未手工结束
     */
	@TableField("activity_hand_end_time")
	private Long activityHandEndTime;
    /**
     * 参与人数
     */
	@TableField("activity_member_count")
	private Integer activityMemberCount;
    /**
     * 删除状态：-1删除、0正常
     */
	@TableField("del_state")
	private Integer delState;
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
	/**
     * 已下单件数
     */
	@TableField("ordered_product_count")
	private Integer orderedProductCount;


	public Integer getOrderedProductCount() {
		return orderedProductCount;
	}

	public void setOrderedProductCount(Integer orderedProductCount) {
		this.orderedProductCount = orderedProductCount;
	}

	public String getShopProductShowcaseImgs() {
		return shopProductShowcaseImgs;
	}

	public void setShopProductShowcaseImgs(String shopProductShowcaseImgs) {
		this.shopProductShowcaseImgs = shopProductShowcaseImgs;
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

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public Long getShopProductId() {
		return shopProductId;
	}

	public void setShopProductId(Long shopProductId) {
		this.shopProductId = shopProductId;
	}

	public String getClothesNumber() {
		return clothesNumber;
	}

	public void setClothesNumber(String clothesNumber) {
		this.clothesNumber = clothesNumber;
	}

	public String getShopProductName() {
		return shopProductName;
	}

	public void setShopProductName(String shopProductName) {
		this.shopProductName = shopProductName;
	}

	public String getShopProductMainimg() {
		return shopProductMainimg;
	}

	public void setShopProductMainimg(String shopProductMainimg) {
		this.shopProductMainimg = shopProductMainimg;
	}

	public Double getActivityProductPrice() {
		return activityProductPrice;
	}

	public void setActivityProductPrice(Double activityProductPrice) {
		this.activityProductPrice = activityProductPrice;
	}

	public Double getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(Double activityPrice) {
		this.activityPrice = activityPrice;
	}

	public Integer getActivityProductCount() {
		return activityProductCount;
	}

	public void setActivityProductCount(Integer activityProductCount) {
		this.activityProductCount = activityProductCount;
	}

//	public Integer getSurplusActivityProductCount() {
//		return surplusActivityProductCount;
//	}
//
//	public void setSurplusActivityProductCount(Integer surplusActivityProductCount) {
//		this.surplusActivityProductCount = surplusActivityProductCount;
//	}

	public Long getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Long activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public Long getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Long activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public Long getActivityHandEndTime() {
		return activityHandEndTime;
	}

	public void setActivityHandEndTime(Long activityHandEndTime) {
		this.activityHandEndTime = activityHandEndTime;
	}

	public Integer getActivityMemberCount() {
		return activityMemberCount;
	}

	public void setActivityMemberCount(Integer activityMemberCount) {
		this.activityMemberCount = activityMemberCount;
	}

	public Integer getDelState() {
		return delState;
	}

	public void setDelState(Integer delState) {
		this.delState = delState;
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

//	public Integer getBuyActivityProductCount() {
//		return buyActivityProductCount;
//	}
//
//	public void setBuyActivityProductCount(Integer buyActivityProductCount) {
//		this.buyActivityProductCount = buyActivityProductCount;
//	}

	//获取当前活动状态
	public String haveActivityStatus(){
		String activityStatus = "";
		//活动状态：待开始，进行中，已结束（手工结束、过期结束）
		if(activityHandEndTime  > 0){
			activityStatus = "已结束";
		}else if(activityEndTime <= System.currentTimeMillis()){
			activityStatus = "已结束";   
		}else if(activityStartTime <= System.currentTimeMillis()){
			activityStatus = "进行中";   
		}else{
			activityStatus = "待开始";   
		}
		return activityStatus;
	}
	
	//获取当前活动状态
	public int haveActivityStatusInt(){
		int activityStatus = 0;
		//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
		if(activityHandEndTime  > 0){
			activityStatus = 3;
		}else if(activityEndTime <= System.currentTimeMillis()){
			activityStatus = 3;   
		}else if(activityStartTime <= System.currentTimeMillis()){
			activityStatus = 2;   
		}else{
			activityStatus = 1;   
		}
		return activityStatus;
	}
}