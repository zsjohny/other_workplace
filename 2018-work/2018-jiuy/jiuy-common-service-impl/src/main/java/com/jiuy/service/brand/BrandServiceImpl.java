package com.jiuy.service.brand;

import com.jiuy.mapper.brand.BrandMapper;
import com.jiuy.model.brand.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/3 10:05
 * @Copyright 玖远网络
 */

@Service("brandService")
public class BrandServiceImpl implements IBrandService {


    @Autowired
    private BrandMapper brandMapper;

    /**
     * 通过id 获取
     *
     * @param id 主键
     * @date 2018/5/3 10:07
     * @author Aison
     */
    @Override
    public Brand getById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过id 获取
     *
     * @param brandId 品牌id
     * @date 2018/5/3 10:07
     * @author Aison
     */
    @Override
    public Brand getByBrandId(Long brandId) {
        Brand brand = new Brand();
        brand.setBrandId(brandId);
        List<Brand> brands = brandMapper.selectList(brand);
        return brands.size() > 0 ? brands.get(0) : null;
    }
}
