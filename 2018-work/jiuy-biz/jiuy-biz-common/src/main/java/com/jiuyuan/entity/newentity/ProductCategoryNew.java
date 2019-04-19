package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 商品分类关联表
 * @author 赵兴林
 * @since 2017-10-24
 */
@TableName("yjj_ProductCategory")
public class ProductCategoryNew extends Model<ProductCategoryNew> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 产品ID
     */
	private Long ProductId;
    /**
     * 分类ID
     */
	private Long CategoryId;
    /**
     * 状态
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 修改时间
     */
	private Long UpdateTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getProductId() {
		return ProductId;
	}

	public void setProductId(Long ProductId) {
		this.ProductId = ProductId;
	}

	public Long getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(Long CategoryId) {
		this.CategoryId = CategoryId;
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
