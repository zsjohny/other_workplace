package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyAddress;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.impl.BaseServiceImpl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.order.mapper.ProxyAddressMapper;
import com.e_commerce.miscroservice.order.service.proxy.ProxyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProxyAddressServiceImpl extends BaseServiceImpl<ProxyAddress> implements ProxyAddressService {

    private Log logger = Log.getInstance(ProxyAddressServiceImpl.class);

    @Autowired
    ProxyAddressMapper proxyAddressMapper;

    @Override
    public List<ProxyAddress> getAddressByUserId(Long userId, int isDefault) {
        return proxyAddressMapper.getAddressByUserId(userId,isDefault);
    }

    @Override
    public List<ProxyAddress> getAddressByUserId(Long userId) {
        return proxyAddressMapper.getAddressByUserIdN(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignDefaultAddress(Long id, Integer isDefault) {

        Optional<ProxyAddress> history = selectById (id);
        if (! history.isPresent ()) {
            logger.warn ("设置默认地址,未找到地址id={}", id);
            return;
        }

        if(ObjectUtils.nullSafeEquals (isDefault, 1)){
            //先把默认的设为非默认
            ProxyAddress updInfo = new ProxyAddress ();
            updInfo.setIsDefault (0);
            MybatisOperaterUtil.getInstance ().update (
                    updInfo,
                    new MybatisSqlWhereBuild (ProxyAddress.class)
                    .eq (ProxyAddress::getUserId, history.get ().getUserId ())
                    .eq (ProxyAddress::getDelStatus, StateEnum.NORMAL)
                    .eq (ProxyAddress::getIsDefault, 1)
            );
        }

        //更新默认值
        ProxyAddress updInfo = new ProxyAddress ();
        updInfo.setIsDefault (isDefault);
        MybatisOperaterUtil.getInstance ().update (
                updInfo,
                new MybatisSqlWhereBuild (ProxyAddress.class)
                        .eq (ProxyAddress::getId, id)
        );

    }


    /**
     * 更新地址
     *
     * @param proxyAddress proxyAddress
     * @return void
     * @author hyq, Charlie
     * @date 2018/11/7 15:19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAddress(ProxyAddress proxyAddress) {
        Integer isDefault = proxyAddress.getIsDefault ();
        if (isDefault == null || proxyAddress.getUserId () == null) {
            logger.error ("修改地址, 参数不合法  proxyAddress={}", proxyAddress);
            return;
        }
        if(ObjectUtils.nullSafeEquals (1, isDefault)){
            //先把默认的设为非默认
            ProxyAddress updInfo = new ProxyAddress ();
            updInfo.setIsDefault (0);
            MybatisOperaterUtil.getInstance ().update (
                    updInfo,
                    new MybatisSqlWhereBuild (ProxyAddress.class)
                            .eq (ProxyAddress::getUserId, proxyAddress.getUserId ())
                            .eq (ProxyAddress::getDelStatus, StateEnum.NORMAL)
                            .eq (ProxyAddress::getIsDefault, 1)
            );
        }
        insertSelective(proxyAddress);
    }
}
