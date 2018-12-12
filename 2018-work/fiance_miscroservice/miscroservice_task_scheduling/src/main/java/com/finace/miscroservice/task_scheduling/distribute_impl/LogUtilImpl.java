package com.finace.miscroservice.task_scheduling.distribute_impl;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.distribute_task.util.LogUtil;

/**
 * 日志的帮助类
 */
public class LogUtilImpl implements LogUtil {
    Log log;

    @Override
    public LogUtil setInstance(Class<?> aClass) {
        LogUtilImpl logUtil = new LogUtilImpl();
        log = Log.getInstance(aClass);
        return logUtil;
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(s, objects);
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(s, objects);
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(s, objects);
    }
}
