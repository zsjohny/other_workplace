package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-10
 */
@TableName("store_ShoppingCart")
public class StoreShoppingCartNew extends Model<StoreShoppingCartNew> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 门店id
     */
	private Long StoreId;
    /**
     * 商品id
     */
	private Long ProductId;
    /**
     * 对应ProductSKU的id
     */
	private Long SkuId;
    /**
     * 购买数量
     */
	private Integer BuyCount;
    /**
     * 在购物车中是否被选中:0未选中,1选中
     */
	private Integer IsSelected;
    /**
     * 状态:-1删除，0正常
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 统计识别码
     */
	private Long StatisticsId;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public Long getProductId() {
		return ProductId;
	}

	public void setProductId(Long ProductId) {
		this.ProductId = ProductId;
	}

	public Long getSkuId() {
		return SkuId;
	}

	public void setSkuId(Long SkuId) {
		this.SkuId = SkuId;
	}

	public Integer getBuyCount() {
		return BuyCount;
	}

	public void setBuyCount(Integer BuyCount) {
		this.BuyCount = BuyCount;
	}

	public Integer getIsSelected() {
		return IsSelected;
	}

	public void setIsSelected(Integer IsSelected) {
		this.IsSelected = IsSelected;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
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

	public Long getStatisticsId() {
		return StatisticsId;
	}

	public void setStatisticsId(Long StatisticsId) {
		this.StatisticsId = StatisticsId;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
