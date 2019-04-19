package com.e_commerce.miscroservice.store.dao.impl;


import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.store.dao.SupplierUserDao;
import com.e_commerce.miscroservice.store.entity.vo.SupplierUser;
import org.springframework.stereotype.Repository;


/**
 * 产品表
 */
@Repository
public class SupplierUserDaoImpl implements SupplierUserDao {


    @Override
    public SupplierUser getSupplierUserInfo(long supplierId) {
       return MybatisOperaterUtil.getInstance().findOne(new SupplierUser(), new MybatisSqlWhereBuild(SupplierUser.class).eq(SupplierUser::getId,supplierId));
    }
}
