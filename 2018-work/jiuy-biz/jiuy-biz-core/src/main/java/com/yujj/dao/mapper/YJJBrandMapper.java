package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.entity.Brand;

@DBMaster
public interface YJJBrandMapper {

    @MapKey("brandId")
    Map<Long, Brand> getBrandMap(@Param("brandIds") Collection<Long> brandIds);
    
	/**
	 * @author DongZhong
	 */
    @MapKey("brandId")
    Map<Long, Brand> getBrands();
    
    List<Brand> getBrandListByArr(@Param("arr") String [] arr);
    
    List<BrandVO> getBrandListShow(@Param("searchBrand") String searchBrand, @Param("pageQuery") PageQuery pageQuery, @Param("type") int type, @Param("userId") long userId);

}
