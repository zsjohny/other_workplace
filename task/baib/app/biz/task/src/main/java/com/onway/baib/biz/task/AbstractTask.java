package com.onway.baib.biz.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.onway.common.lang.StringUtils;
import com.onway.platform.common.helper.TimeKey;
import com.onway.platform.common.utils.LogUtil;

/**
 * 抽象task处理类
 * 
 * @author guangdong.li
 * @version $Id: AbstractTask.java, v 0.1 7 Jan 2016 13:53:15 guangdong.li Exp $
 */
public abstract class AbstractTask {

    /** logger  */
    protected static final Logger                       logger   = Logger
                                                                     .getLogger(AbstractTask.class);

    /** 线程防重复执行管理    */
    protected static final Map<Class<?>, AtomicBoolean> lockMaps = new ConcurrentHashMap<Class<?>, AtomicBoolean>();

    /**
     * 任务执行,增加TimeKey,方便跟进task执行业务状况
     */
    public void execute() {
        try {
            if (!preProcess()) {
                LogUtil.warn(logger, this.getClass().getSimpleName() + "执行终端，原因：已有task执行中...");
                return;
            }

            if (canProcess()) {
                LogUtil.info(logger, "开始执行task,taskName=>" + this.getClass().getSimpleName());
                process();
            }
        } catch (Throwable e) {
            LogUtil.error(logger, e, "执行task异常,taskName=>" + this.getClass().getSimpleName());
        } finally {
            afterProcess();
        }
    }

    /**
     * 业务处理前置判断
     */
    protected abstract boolean canProcess();

    /**
     * 业务处理
     */
    protected abstract void process();

    /**
     * 打印info日志
     * 
     * @param message
     */
    protected void printInfoLog(String message) {
        if (StringUtils.isNotBlank(message)) {
            LogUtil.info(logger, message);
        }
    }

    /**
     * task执行前置 
     * <pre>
     *      放置重复执行处理,首次执行则插入执行class,再次执行拒绝
     * </pre>
     * 
     * @return
     */
    protected boolean preProcess() {
        // 构建时间搓
        TimeKey.start();
        printInfoLog("执行 " + this.getClass().getSimpleName() + " 开始...");
        //如果当前的值为true表示可以执行此task,并将状态置为false不可执行状态
        if (!lockMaps.containsKey(this.getClass())) {
            lockMaps.put(this.getClass(), new AtomicBoolean(true));
            return true;
        }
        return false;
    }

    /**
     * 删除执行限制
     */
    protected void afterProcess() {
        printInfoLog("执行 " + this.getClass().getSimpleName() + " 结束...");

        //如果当前处于不可执行状态,则将其置为可执行状态,表示当前线程已执行完,可以允许其它线程再来执行
        lockMaps.remove(this.getClass());
        // 清除时间搓
        TimeKey.clear();
    }

}
