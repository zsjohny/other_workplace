package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品上下架操作记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-30
 */
@TableName("shop_product_updownsold_log")
public class ProductUpdownsoldLog extends Model<ProductUpdownsoldLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品ID
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 商品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 商品款号
     */
	@TableField("product_number")
	private String productNumber;
    /**
     * SKUID
     */
	@TableField("product_sku_id")
	private Long productSkuId;
    /**
     * 商品尺码名称
     */
	@TableField("size_name")
	private String sizeName;
    /**
     * 商品颜色名称
     */
	@TableField("color_name")
	private String colorName;
    /**
     * 上下架类型：0(下架)、1(上架)
     */
	@TableField("updownsold_type")
	private Integer updownsoldType;
    /**
     * 上下架备注
     */
	private String note;
    /**
     * 操作人ID
     */
	@TableField("action_user_id")
	private Long actionUserId;
    /**
     * 操作人账号
     */
	@TableField("action_user_account")
	private String actionUserAccount;
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

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public Long getProductSkuId() {
		return productSkuId;
	}

	public void setProductSkuId(Long productSkuId) {
		this.productSkuId = productSkuId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public Integer getUpdownsoldType() {
		return updownsoldType;
	}

	public void setUpdownsoldType(Integer updownsoldType) {
		this.updownsoldType = updownsoldType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Long actionUserId) {
		this.actionUserId = actionUserId;
	}

	public String getActionUserAccount() {
		return actionUserAccount;
	}

	public void setActionUserAccount(String actionUserAccount) {
		this.actionUserAccount = actionUserAccount;
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

}
