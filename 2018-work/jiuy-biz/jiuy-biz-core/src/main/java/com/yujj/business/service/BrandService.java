package com.yujj.business.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.dao.mapper.YJJBrandMapper;
import com.yujj.entity.Brand;

@Service
public class BrandService {

	@Autowired
	private YJJBrandMapper brandMapper;
	
    public Brand getBrand(Long brandId) {
        return getBrandMap(CollectionUtil.createSet(brandId)).get(brandId);
	}

    public Map<Long, Brand> getBrandMap(Collection<Long> brandIds) {
        return brandMapper.getBrandMap(brandIds);
    }
    
    public Map<Long, Brand> getBrands() {
        return brandMapper.getBrands();
    }
    
    public List<Brand> getBrandListByArr(String [] arr) {
    	return brandMapper.getBrandListByArr(arr);
    }
    
    public List<BrandVO> getBrandListShow(String searchBrand, PageQuery pageQuery, int type ,long userId) {
    	return brandMapper.getBrandListShow(searchBrand, pageQuery, type , userId);
    }


}
