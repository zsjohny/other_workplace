package com.jiuyuan.entity.miaosha;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 秒杀商品表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-21
 */
@TableName("shop_miaosha_product")
public class MiaoshaProduct extends Model<MiaoshaProduct> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 平台商品ID
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 商品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 商品主图
     */
	@TableField("product_img")
	private String productImg;
    /**
     * 原价
     */
	private BigDecimal price;
    /**
     * 秒杀价
     */
	@TableField("miaosha_price")
	private BigDecimal miaoshaPrice;
    /**
     * 状态:-1删除、0未开始、1已开始、2已结束
     */
	private Integer state;
    /**
     * 数量
     */
	@TableField("miaosha_count")
	private Long miaoshaCount;
	  /**
     * 秒杀开始时间
     */
	@TableField("miaosha_start_time")
	private Long miaoshaStartTime;
	
	  /**
     * 秒杀开始时间
     */
	 @TableField(exist = false)
	private String miaoshaStartTimeStr;
	
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMiaoshaPrice() {
		return miaoshaPrice;
	}

	public void setMiaoshaPrice(BigDecimal miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getMiaoshaCount() {
		return miaoshaCount;
	}

	public void setMiaoshaCount(Long miaoshaCount) {
		this.miaoshaCount = miaoshaCount;
	}

	public Long getMiaoshaStartTime() {
		return miaoshaStartTime;
	}

	public void setMiaoshaStartTime(Long miaoshaStartTime) {
		this.miaoshaStartTime = miaoshaStartTime;
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

	public String getMiaoshaStartTimeStr() {
		return miaoshaStartTimeStr;
	}

	public void setMiaoshaStartTimeStr(String miaoshaStartTimeStr) {
		this.miaoshaStartTimeStr = miaoshaStartTimeStr;
	}

}
