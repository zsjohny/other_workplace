package com.finace.miscroservice;


import com.finace.miscroservice.commons.annotation.ServiceStart;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@EnableConfigServer
@ServiceStart
public class ConfigMain {
    public static void main(String[] args) {

        ApplicationContextUtil.run(ConfigMain.class, ApplicationContextUtil.copy(args, ApplicationContextUtil.EXCLUDE_START_PARAMS));
    }
}
