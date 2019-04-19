package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductAudit;
import com.jiuyuan.entity.newentity.ProductNew;


@DBMaster
public interface ProductAuditMapper extends BaseMapper<ProductAudit> {
	/**
	 * 
	 * @param page
	 * @param beginTime
	 * @param endTime
	 * @param orderByField
	 * @param isAsc
	 * @return
	 */
	List<ProductNew> getSearchProductList(@Param("page") Page<ProductNew> page,
			@Param("keyword") String keyword,  @Param("orderByField") String orderByField,@Param("isAsc") boolean isAsc);
	
	/**
	 * 
	 * @return
	 */
	ProductNew getProductById(@Param("productId") int productId);


	
}
