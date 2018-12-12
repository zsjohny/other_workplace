package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.ServiceStart;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableEurekaClient
@EnableZuulProxy
@ServiceStart
public class GetWayCenterMain {


    public static void main(String[] args) {
        ApplicationContextUtil.run(GetWayCenterMain.class, args);
    }
}
