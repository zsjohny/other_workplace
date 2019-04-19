package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品ID和分类ID对应表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-31
 */
@TableName("yjj_productIdCategoryId")
public class ProductIdCategoryId extends Model<ProductIdCategoryId> {

    private static final long serialVersionUID = 1L;

    /**
     *  商品ID
     */
	@TableId(value="productId", type= IdType.AUTO)
	private Long productId;
    /**
     * 分类ID
     */
	private Long categoryId;
    

	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	@Override
	protected Serializable pkVal() {
		return this.productId;
	}

}
