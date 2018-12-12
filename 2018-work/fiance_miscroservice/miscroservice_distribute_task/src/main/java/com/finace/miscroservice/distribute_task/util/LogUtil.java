package com.finace.miscroservice.distribute_task.util;

/**
 * 日志的帮助类
 */
public interface LogUtil {

    /**
     * 获取log实例
     *
     * @param className 类的名称
     * @return
     */
    LogUtil setInstance(Class<?> className);


    /**
     * Info 日志记录级别
     *
     * @param msg  详细信息
     * @param info 记录的具体信息
     */
    void info(String msg, Object... info);

    /**
     * Warn 日志记录级别
     *
     * @param msg  详细信息
     * @param info 记录的具体信息
     */
    void warn(String msg, Object... info);

    /**
     * Error 日志记录级别
     *
     * @param msg  详细信息
     * @param info 记录的具体信息
     */
    void error(String msg, Object... info);


}
