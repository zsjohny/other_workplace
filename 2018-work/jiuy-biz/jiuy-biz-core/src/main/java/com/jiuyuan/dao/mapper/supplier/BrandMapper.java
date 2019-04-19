package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.Brand;

@DBMaster
public interface BrandMapper {
    
    @MapKey("brandId")
    Map<Long, Brand> getBrands();

    List<BrandVO> getBrandListShow(@Param("searchBrand") String searchBrand,
    		@Param("pageQuery") PageQuery pageQuery, @Param("type") int type, 
    		@Param("userId") long userId, @Param("brandType") int brandType);

	Brand getBrand(@Param("brandId")long brandId);

	List<BrandLogo> getBrandLogos();

	List<BrandVO> getBrandListShowHistory(@Param("userId")Long userId, @Param("pageQuery")PageQuery pageQuery);
}