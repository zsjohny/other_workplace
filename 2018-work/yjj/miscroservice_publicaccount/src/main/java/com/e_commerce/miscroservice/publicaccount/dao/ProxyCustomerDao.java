package com.e_commerce.miscroservice.publicaccount.dao;



import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;

import java.util.List;


/**
 * 代理商
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:03
 * @Copyright 玖远网络
 */
public interface ProxyCustomerDao{

    static ProxyCustomer build(ProxyCustomerAudit audit) {
        ProxyCustomer proxyCustomer = new ProxyCustomer ();
        proxyCustomer.setAddressDetail (audit.getAddressDetail ());
        proxyCustomer.setCity (audit.getCity ());
        proxyCustomer.setCounty (audit.getCounty ());
        proxyCustomer.setIdCardNo (audit.getIdCardNo ());
        proxyCustomer.setPhone (audit.getPhone ());
        proxyCustomer.setProvince (audit.getProvince ());
        proxyCustomer.setType (audit.getType ());
        proxyCustomer.setUserId (audit.getRecommonUserId ());
        proxyCustomer.setName (audit.getName ());
        return proxyCustomer;
    }

    /**
     * 查询该地方已有的市县代理
     *
     * @param type type
     * @param province province
     * @param city city
     * @param county county
     * @return java.util.List<com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomer>
     * @author Charlie
     * @date 2018/9/21 13:57
     */
    List<ProxyCustomer> selectByTerritoryAndType(Integer type, String province, String city, String county);



    /**
     * 查询用户已有的代理商账号
     *
     * @param phone 手机号
     * @return java.util.List<com.e_commerce.miscroservice.operate.po.proxy.ProxyCustomer>
     * @author Charlie
     * @date 2018/9/21 14:52
     */
    List<ProxyCustomer> selectUserProxy(String phone);

    /**
     * 修改县级代理为市级代理
     * @param proxyCustomerUserId proxyCustomerUserId
     * @author Charlie
     * @date 2018/9/27 11:28
     */
    int updateProxyCounty2City(Long proxyCustomerUserId);


    /**
     * 根据userId查询未删除的代理
     *
     * @param userId userId
     * @return com.e_commerce.miscroservice.publicaccount.po.proxy.ProxyCustomer
     * @author Charlie
     * @date 2018/9/27 11:28
     */
    ProxyCustomer selectByUserId(Long userId);

    List<ProxyCustomer> customerList(ProxyCustomerQuery query);
}
