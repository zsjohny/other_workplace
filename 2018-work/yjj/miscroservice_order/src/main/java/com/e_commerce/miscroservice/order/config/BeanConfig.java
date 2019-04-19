package com.e_commerce.miscroservice.order.config;

import com.e_commerce.miscroservice.order.utils.PayConstantPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/15 10:53
 * @Copyright 玖远网络
 */
@Configuration
public class BeanConfig{


    @Value("${inShop.storeId}")
    private Long inShopStoreId;


    @Value ("${wxPay.inShopWxPayConfig.notifyUrl}")
    private String inShopWxPayNotifyUrl;


    /**
     * 支付配置
     *
     * @return com.e_commerce.miscroservice.order.utils.PayConstantPool
     * @author Charlie
     * @date 2018/12/15 11:01
     */
    @Bean
    public PayConstantPool createPayConstantPool() {
        PayConstantPool pool = new PayConstantPool ();
        pool.setInShopStoreId (inShopStoreId);
        pool.setInShopWxPayNotifyUrl (inShopWxPayNotifyUrl);
        return pool;
    }



}
