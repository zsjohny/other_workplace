package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.ServiceStart;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@ServiceStart
public class ServiceCenterV1Main {


    public static void main(String[] args) {
        ApplicationContextUtil.run(ServiceCenterV1Main.class, ApplicationContextUtil.copy(args, ApplicationContextUtil.EXCLUDE_START_PARAMS));
    }
}
