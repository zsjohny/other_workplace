package com.e_commerce.miscroservice.order.service.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyAddress;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.BaseService;

import java.util.List;

/**
 * 描述 代理商品查询
 * @date 2018/9/18 17:23
 * @return
 */
public interface ProxyAddressService extends BaseService<ProxyAddress> {

    List<ProxyAddress> getAddressByUserId(Long userId,int isDefault);

    List<ProxyAddress> getAddressByUserId(Long userId);

    /**
     * 配置默认地址
     * @param id
     * @param isDefault
     */
    void assignDefaultAddress(Long id, Integer isDefault);


    /**
     * 更新地址
     *
     * @param proxyAddress proxyAddress
     * @return void
     * @author hyq, Charlie
     * @date 2018/11/7 15:19
     */
    void updateAddress(ProxyAddress proxyAddress);

}
