package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Start
@EnableDiscoveryClient
public class PublicaccountStartMain {

    public static void main(String[] args) {
        ApplicationContextUtil.run(PublicaccountStartMain.class, Boolean.FALSE, args);
    }
}
