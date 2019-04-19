package com.wxa.commons;

import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.StoreBusinessNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommonsQueryStoreBuniness {

    @Autowired
    private StoreBusinessNewService storeBusinessNewService;


    private Map<Long, StoreBusiness> storeBusinessCache = new ConcurrentHashMap<>();


    /**
     * @param storeId 商家Id
     * @return
     */
    public StoreBusiness getStoreBusinessById(Long storeId) {

        StoreBusiness storeBusiness = storeBusinessNewService.
                getStoreBusinessById(storeId);

        return storeBusiness;

    }

}
