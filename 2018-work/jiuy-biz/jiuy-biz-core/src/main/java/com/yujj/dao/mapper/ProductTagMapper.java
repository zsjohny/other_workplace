package com.yujj.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.product.ProductTagVO;

@DBMaster
public interface ProductTagMapper {

	/**
	 * @author DongZhong
	 */
	@MapKey("productId")
	Map<Long, ProductTagVO> getProductTagNames();
    
}
