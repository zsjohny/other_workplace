package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.store.dao.BrandDao;
import com.e_commerce.miscroservice.store.entity.vo.Brand;
import org.springframework.stereotype.Repository;


/**
 * 品牌
 */
@Repository
public class BrandDaoImpl implements BrandDao {


    @Override
    public Brand getBrandByBrandId(Long brandId) {
        return MybatisOperaterUtil.getInstance().findOne(new Brand(), new MybatisSqlWhereBuild(Brand.class).eq(Brand::getId,brandId));
    }
}
