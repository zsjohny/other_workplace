package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.Start;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;

@Start
public class OfficialWebsiteStartMain {

    public static void main(String[] args) {

        ApplicationContextUtil.run(OfficialWebsiteStartMain.class, args);
    }
}
