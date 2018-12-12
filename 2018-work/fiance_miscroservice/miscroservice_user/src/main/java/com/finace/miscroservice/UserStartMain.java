package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.Start;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;

@Start
public class UserStartMain {

    public static void main(String[] args)
    {
        ApplicationContextUtil.run(UserStartMain.class, args);
    }
}
