package com.finace.miscroservice;

import com.finace.miscroservice.commons.annotation.Start;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;

@Start
public class TaskSchedulingMain {


    public static void main(String[] args) {
        ApplicationContextUtil.run(TaskSchedulingMain.class, ApplicationContextUtil.copy(args, ApplicationContextUtil.EXCLUDE_START_PARAMS));

    }
}
