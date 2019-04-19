package com.store.service;

import com.store.entity.ShopStoreOrder;

public interface OrderHandler {

    void postOrderCreation(ShopStoreOrder order, String version);

    void postOrderCancel(ShopStoreOrder order, String version);
    
    void updateSaleCount(ShopStoreOrder order, String version);
    
}