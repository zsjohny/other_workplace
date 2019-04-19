package com.jiuyuan.dao.mapper.supplier;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ProductDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@DBMaster
public interface ProductDetailMapper extends BaseMapper<ProductDetail> {

	List<ProductDetail> selectProductDetailList(@Param("productId") Long productId);



}
