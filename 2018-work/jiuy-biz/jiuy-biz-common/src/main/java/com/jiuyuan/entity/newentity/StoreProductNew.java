package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 门店商品
 * </p>
 *
 * @author  qiuyuefan
 * @since 2017-11-28
 */
@TableName("store_Product")
public class StoreProductNew extends Model<StoreProductNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * storeId
     */
	private Long StoreId;
    /**
     * productId
     */
	private Long ProductId;
    /**
     * skuId
     */
	private Long SkuId;
    /**
     * �ϼ�����
     */
	private Integer OnSaleCount;
    /**
     * ������
     */
	private Integer OffSaleCount;
    /**
     * ״̬ 0������ -1��ɾ��
     */
	private Integer Status;
    /**
     * ����ʱ��
     */
	private Long CreateTime;
    /**
     * ����ʱ��
     */
	private Long UpdateTime;


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

	public Integer getOnSaleCount() {
		return OnSaleCount;
	}

	public void setOnSaleCount(Integer OnSaleCount) {
		this.OnSaleCount = OnSaleCount;
	}

	public Integer getOffSaleCount() {
		return OffSaleCount;
	}

	public void setOffSaleCount(Integer OffSaleCount) {
		this.OffSaleCount = OffSaleCount;
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

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
