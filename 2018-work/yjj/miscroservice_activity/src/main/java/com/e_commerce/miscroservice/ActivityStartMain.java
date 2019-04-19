package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;

@Start
public class ActivityStartMain {

    public static void main(String[] args) {
        ApplicationContextUtil.run(ActivityStartMain.class, Boolean.FALSE, args);
    }
}
