package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.ServiceStart;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@ServiceStart
public class AuthorizeMain {
    public static void main(String[] args) {
        ApplicationContextUtil.run(AuthorizeMain.class, args);
    }

}
