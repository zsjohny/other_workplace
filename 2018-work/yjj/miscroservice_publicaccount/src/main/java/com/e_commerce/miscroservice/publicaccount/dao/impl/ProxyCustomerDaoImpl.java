package com.e_commerce.miscroservice.publicaccount.dao.impl;


import com.e_commerce.miscroservice.commons.entity.application.BasePage;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.publicaccount.dao.ProxyCustomerDao;
import com.e_commerce.miscroservice.publicaccount.entity.enums.ProxyCustomerType;
import com.e_commerce.miscroservice.publicaccount.mapper.ProxyCustomerMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 代理商
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:03
 * @Copyright 玖远网络
 */
@Component
public class ProxyCustomerDaoImpl implements ProxyCustomerDao{


    private static final int NORMAL = 0;

    @Autowired
    private ProxyCustomerMapper proxyCustomerMapper;


    /**
     * 查询该地方已有的市县代理
     *
     * @param type     type
     * @param province province
     * @param city     city
     * @param county   county
     * @return java.util.List<com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomer>
     * @author Charlie
     * @date 2018/9/21 13:57
     */
    @Override
    public List<ProxyCustomer> selectByTerritoryAndType(Integer type, String province, String city, String county) {
        //地域已被代理
        MybatisSqlWhereBuild filter = new MybatisSqlWhereBuild (ProxyCustomer.class)
                .eq (ProxyCustomer::getType, type)
                .eq (ProxyCustomer::getDelStatus, NORMAL)
                .eq (ProxyCustomer::getProvince, province)
                .eq (ProxyCustomer::getCity, city);
        if (type == ProxyCustomerType.COUNTY.getCode ()) {
            filter.eq (ProxyCustomer::getCounty, county);
        }
        return MybatisOperaterUtil.getInstance ().finAll (new ProxyCustomer (), filter);
    }


    /**
     * 查询用户已有的代理商账号
     *
     * @param phone phone
     * @return java.util.List<com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomer>
     * @author Charlie
     * @date 2018/9/21 14:52
     */
    @Override
    public List<ProxyCustomer> selectUserProxy(String phone) {
        MybatisSqlWhereBuild filter = new MybatisSqlWhereBuild (ProxyCustomer.class)
                .eq (ProxyCustomer::getDelStatus, NORMAL)
                .eq (ProxyCustomer::getPhone, phone);
        return MybatisOperaterUtil.getInstance ().finAll (new ProxyCustomer (), filter);
    }


    /**
     * 将县级代理升级到市级代理
     *
     * @param proxyCustomerId recommonUserId
     * @return int
     * @author Charlie
     * @date 2018/9/21 15:44
     */
    @Override
    public int updateProxyCounty2City(Long proxyCustomerId) {
        ProxyCustomer updateInfo = new ProxyCustomer ();
        updateInfo.setType (ProxyCustomerType.CITY.getCode ());
        return MybatisOperaterUtil.getInstance ().update (
                updateInfo,
                new MybatisSqlWhereBuild (ProxyCustomer.class)
                        .eq (ProxyCustomer::getId, proxyCustomerId)
                        .eq (ProxyCustomer::getDelStatus, NORMAL)
                        .eq (ProxyCustomer::getType, ProxyCustomerType.COUNTY.getCode ())
        );
    }




    /**
     * 根据userId查询未删除的代理
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/27 11:28
     */
    @Override
    public ProxyCustomer selectByUserId(Long userId) {
        if (! BeanKit.gt0 (userId)) {
            return null;
        }
        return MybatisOperaterUtil.getInstance ().findOne (
                new ProxyCustomer (),
                new MybatisSqlWhereBuild (ProxyCustomer.class)
                        .eq (ProxyCustomer::getUserId, userId)
                        .eq (ProxyCustomer::getDelStatus, StateEnum.NORMAL)
        );
    }

    @Override
    public List<ProxyCustomer> customerList(ProxyCustomerQuery query) {
        PageHelper.startPage(query.getPageNum(),query.getPageSize());
        return proxyCustomerMapper.customerList(query);
    }
}
