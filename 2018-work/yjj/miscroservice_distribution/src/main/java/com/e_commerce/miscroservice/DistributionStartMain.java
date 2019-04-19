package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;

@Start
public class DistributionStartMain {

    public static void main(String[] args) {

        ApplicationContextUtil.run(DistributionStartMain.class, Boolean.FALSE, args);
    }
}
